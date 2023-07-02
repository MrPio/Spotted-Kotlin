package it.univpm.spottedkotlin.view.fragments

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.MapFragmentBinding
import it.univpm.spottedkotlin.enums.Numbers
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.enums.Settings
import it.univpm.spottedkotlin.extension.MultiOverlayItem
import it.univpm.spottedkotlin.extension.MyMapView
import it.univpm.spottedkotlin.extension.function.*
import it.univpm.spottedkotlin.interfaces.OnPanAndZoomListener
import it.univpm.spottedkotlin.managers.BitmapManager
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.view.ViewPostActivity
import it.univpm.spottedkotlin.viewmodel.MapViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import kotlin.concurrent.thread


class MapFragment : Fragment() {
	private lateinit var binding: MapFragmentBinding
	private val viewModel: MapViewModel by viewModels()
	private lateinit var markers: List<OverlayItem>
	private val multiItems: MutableList<OverlayItem> = mutableListOf()
	private lateinit var map: MyMapView
	private lateinit var mapController: IMapController
	private lateinit var markerPlaceholder: BitmapDrawable

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {

		// Require permission for the map
		requireActivity().checkAndAskPermission(WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION)
		Configuration.getInstance()
			.load(requireContext(), requireActivity().getPreferences(Context.MODE_PRIVATE))

		binding = MapFragmentBinding.inflate(inflater, container, false)
		map = binding.mapMap
		mapController = map.controller
		markerPlaceholder = requireContext().loadBitmapDrawable(R.drawable.map_marker, 55, 55)
		binding.mapAddPost.setOnClickListener {
			val mainActivity = (requireActivity() as MainActivity)
			mainActivity.viewModel.currentFragment.value = 2
			(mainActivity.viewModel.fragments[2] as AddPostFragment).apply {
				latitude = (map.mapCenter as GeoPoint).latitude
				longitude = (map.mapCenter as GeoPoint).longitude
			}
		}

		binding.mapLoadingView.loadingViewRoot.visibility = View.VISIBLE
		MainScope().launch {
			markers = viewModel.loadOverlayItems(requireContext())
			initializeMap()
			activity?.runOnUiThread {
				binding.mapLoadingView.loadingViewRoot.visibility = View.GONE
			}
		}

		return binding.root
	}

	// Initialize map start settings. Register onDraw and onZoom listeners
	private fun initializeMap() {
		map.setTileSource(TileSourceFactory.MAPNIK)
		map.setBuiltInZoomControls(false)
		map.setMultiTouchControls(true)

		map.minZoomLevel = 13.0
		map.maxZoomLevel = 20.0
		mapController.setZoom(13.0)
		val startPoint = GeoPoint(43.6100, 13.5134)
		val startZoom = 14.0
		mapController.setCenter(startPoint)
		mapController.animateTo(startPoint, startZoom, 1200)
		val startTime = System.currentTimeMillis()
		map.overlays.add(MyLocationNewOverlay(map))

		map.setOnPanAndZoomListener(object : OnPanAndZoomListener {
			override fun onDraw(geo: GeoPoint) {
				if (!Settings.MAP_BOUNDARY.bool)
					return

				// BOUNDARY CONSTRAINT
				if (System.currentTimeMillis() - startTime < 2000) return
				map.minZoomLevel = startZoom
				val longitudeSpan = 0.25
				val latitudeSpan = 0.3

				val halfWidth = map.longitudeSpanDouble / 2
				val left = geo.longitude - halfWidth
				val right = geo.longitude + halfWidth
				val minLeft = startPoint.longitude - longitudeSpan / 2
				val maxRight = startPoint.longitude + longitudeSpan / 2

				val halfHeight = map.latitudeSpanDouble / 2
				val top = geo.latitude + halfHeight
				val bottom = geo.latitude - halfHeight
				val minBottom = startPoint.latitude - latitudeSpan / 2
				val maxTop = startPoint.latitude + latitudeSpan / 2

				// HORIZONTAL constraints
				if (left < minLeft) {
					mapController.animateTo(
						GeoPoint(
							geo.latitude, minLeft + halfWidth * 1.1
						), map.zoomLevelDouble, 200
					)
				} else if (right > maxRight) {
					mapController.animateTo(
						GeoPoint(
							geo.latitude, maxRight - halfWidth * 1.1
						), map.zoomLevelDouble, 200
					)
				}

				// VERTICAL constraints
				if (bottom < minBottom) {
					mapController.animateTo(
						GeoPoint(
							minBottom + halfHeight * 1.1, geo.longitude
						), map.zoomLevelDouble, 200
					)
				} else if (top > maxTop) {
					mapController.animateTo(
						GeoPoint(
							maxTop - halfHeight * 1.1, geo.longitude
						), map.zoomLevelDouble, 200
					)
				}
			}

			override fun onPan(geo: GeoPoint) {}

			override fun onZoom(zoom: Int) {
				val zooms = mapOf(
					13 to 1000,
					14 to 1000,
					15 to 500,
					16 to 350,
					17 to 150,
					18 to 0,
					19 to 0
				)
				if (markers.size < 2) return

				multiItems.clear()
				val scanned: MutableList<OverlayItem> = mutableListOf()

				// Group all the nearby markers in subsets. The distance is based on the zoom level
				for (first in markers) {
					if (first in scanned)
						continue
					val nearby: MutableSet<OverlayItem> = mutableSetOf()

					// Find all the nearby markers to (first) marker
					for (second in markers) {
						val distance = (first.point as GeoPoint).distanceToAsDouble(second.point)
						if (distance < (zooms[zoom] ?: 0))
							nearby.add(second)
					}

					// Create a MultiMarker if the subset contains at least 2 markers
					if (nearby.size > 1) {
						multiItems.add(
							MultiOverlayItem(first.title, first.snippet, nearby, markerPlaceholder)
						)
						scanned.addAll(nearby)
					}
				}

				// Show all the markers outside every subsets
				multiItems.addAll(markers.filter { !scanned.contains(it) })

				//Load and set MultiMarkers images
				GlobalScope.launch {

					// Load image assets
					val whiteCircleBitmap = RemoteImages.CIRCLE_WHITE.load()
					val markerBitmap = RemoteImages.MAP_MARKER.load()

					// Load MultiMarkers number image
					try {
						for (item in multiItems) {
							if (item !is MultiOverlayItem)
								continue

							// Render the MultiMarker number image
							val size = item.markers.size
							val ten =
								if (size < 100)
									if ((size / 10) % 10 == 0)
										null
									else
										resources.loadBitmap(Numbers.values()[(size / 10) % 10].res)
								else
									resources.loadBitmap(Numbers._9.res)
							val unity =
								if (size < 100)
									resources.loadBitmap(Numbers.values()[size % 10].res)
								else
									resources.loadBitmap(Numbers._9.res)
							val bitmap = BitmapManager.overlay(
								markerBitmap,
								whiteCircleBitmap,
								ten,
								unity,
							)

							// Set the rendered image
							try {
								item.setMarker(
									requireContext().loadBitmapDrawable(bitmap, 160, 160)
								)
							} catch (_: Exception) {
								break
							}
						}
					} catch (_: ConcurrentModificationException) {
					}

					// Request map reload
					map.invalidate()
				}

				map.showMarkers(context, multiItems, ::onMarkerClick)
			}

			override fun onClick(geo: GeoPoint) {
				geo.toString().log()
			}
		})
	}

	// Marker onClick event
	private fun onMarkerClick(index: Int) {
		val item = multiItems[index]
		if (item is MultiOverlayItem)
			mapController.animateTo(item.point, map.zoomLevelDouble + 1.0, 400)
		else {
			val intent = Intent(requireActivity(), ViewPostActivity::class.java)
			intent.putExtra("postUID", item.uid)
			requireActivity().startActivity(intent)
		}
	}

	override fun onResume() {
		super.onResume()
		map.onResume() //needed for compass, my location overlays, v6.0.0 and up
	}

	override fun onPause() {
		super.onPause()
		map.onPause() //needed for compass, my location overlays, v6.0.0 and up
	}
}