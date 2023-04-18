package it.univpm.spottedkotlin.view.fragments

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.MapFragmentBinding
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.extension.MultiOverlayItem
import it.univpm.spottedkotlin.extension.MyMapView
import it.univpm.spottedkotlin.extension.function.checkAndAskPermission
import it.univpm.spottedkotlin.extension.function.loadDrawable
import it.univpm.spottedkotlin.extension.function.log
import it.univpm.spottedkotlin.interfaces.OnPanAndZoomListener
import it.univpm.spottedkotlin.managers.BitmapManager
import it.univpm.spottedkotlin.viewmodel.MapViewModel
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedIconOverlay.OnItemGestureListener
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import kotlin.concurrent.thread
import kotlin.coroutines.coroutineContext


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
		requireActivity().checkAndAskPermission(WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION)
		Configuration.getInstance()
			.load(requireContext(), requireActivity().getPreferences(Context.MODE_PRIVATE))
		binding = MapFragmentBinding.inflate(inflater, container, false)

		map = binding.mapMap
		mapController = map.controller
		markerPlaceholder = BitmapDrawable(
			resources,
			Bitmap.createScaledBitmap(
				(requireContext().loadDrawable(R.drawable.map_marker) as BitmapDrawable).bitmap,
				55,
				55,
				true
			)
		)

		loadMarkers()
		map.setTileSource(TileSourceFactory.MAPNIK)
		map.setBuiltInZoomControls(false)
		map.setMultiTouchControls(true)

		map.minZoomLevel = 13.0
		map.maxZoomLevel = 19.0
		mapController.setZoom(13.0)
		val startPoint = GeoPoint(43.6100, 13.5134)

//		map.showMarkers(context, markers, ::onMarkerClick)

		val startZoom = 14.0
		mapController.setCenter(startPoint)
		mapController.animateTo(startPoint, startZoom, 1200)
		val startTime = System.currentTimeMillis()
		map.overlays.add(MyLocationNewOverlay(map))

		map.setOnPanAndZoomListener(object : OnPanAndZoomListener {
			override fun onDraw(geo: GeoPoint) {
				// BOUNDARY CONSTRAINT
				if (System.currentTimeMillis() - startTime < 2000) return
				map.minZoomLevel = startZoom
				val longitudeSpan = 0.14
				val latitudeSpan = 0.17

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
							geo.latitude, minLeft + halfWidth
						), map.zoomLevelDouble, 200
					)
				} else if (right > maxRight) {
					mapController.animateTo(
						GeoPoint(
							geo.latitude, maxRight - halfWidth
						), map.zoomLevelDouble, 200
					)
				}

				// VERTICAL constraints
				if (bottom < minBottom) {
					mapController.animateTo(
						GeoPoint(
							minBottom + halfHeight, geo.longitude
						), map.zoomLevelDouble, 200
					)
				} else if (top > maxTop) {
					mapController.animateTo(
						GeoPoint(
							maxTop - halfHeight, geo.longitude
						), map.zoomLevelDouble, 200
					)
				}
			}

			override fun onPan(geo: GeoPoint) {
			}

			override fun onZoom(zoom: Int) {
				val zooms = mapOf(
					13 to 1000,
					14 to 1000,
					15 to 600,
					16 to 400,
					17 to 200,
					18 to 100,
					19 to 0
				)
				if (markers.size < 2) return

				multiItems.clear()
				val scanned: MutableList<OverlayItem> = mutableListOf()
				for (first in markers) {
					if (first in scanned)
						continue
					val nearby: MutableSet<OverlayItem> = mutableSetOf()
					for (second in markers) {
						val distance = (first.point as GeoPoint).distanceToAsDouble(second.point)
						if (distance < (zooms[zoom] ?: 0))
							nearby.add(second)
					}
					if (nearby.size > 1) {
						multiItems.add(
							MultiOverlayItem(first.title, first.snippet, nearby, markerPlaceholder)
						)
						scanned.addAll(nearby)
					}
				}
				multiItems.addAll(markers.filter { !scanned.contains(it) })

				thread {
					val whiteCircleBitmap = RemoteImages.CIRCLE_WHITE.load()
					val markerBitmap = RemoteImages.MAP_MARKER.load()
					try {
						for (item in multiItems) {
							if (item !is MultiOverlayItem)
								continue
							val bitmap = BitmapManager.overlay(
								markerBitmap,
								whiteCircleBitmap,
								RemoteImages.AVATAR_20.load(),
							)
							item.setMarker(
								BitmapDrawable(
									resources, Bitmap.createScaledBitmap(bitmap, 160, 160, true)
								)
							)
						}
					} catch (_: ConcurrentModificationException) { }
					map.invalidate()
				}
				map.showMarkers(context, multiItems, ::onMultiMarkerClick)
			}
		})

		return binding.root
	}

	private fun onMarkerClick(index: Int) {
		Toast.makeText(context, "$index", Toast.LENGTH_SHORT).show()
	}

	private fun onMultiMarkerClick(index: Int) {
		Toast.makeText(context, "$index", Toast.LENGTH_SHORT).show()
	}

	private fun loadMarkers() {
		val markers = mutableListOf<OverlayItem>()
		//Il drawable del segnalino
		for (i in 0..49) {
			val item = OverlayItem(
				"Tizio",
				"Tizio",
				GeoPoint(43.6100 + Math.random() * 0.025, 13.5134 + Math.random() * 0.025)
			)

			// Setto il placeholder del segnalino
			item.setMarker(markerPlaceholder)
			markers.add(item)
		}
		thread {
			val whiteCircleBitmap = RemoteImages.CIRCLE_WHITE.load()
			val markerBitmap = RemoteImages.MAP_MARKER.load()
			for (item in markers) {
				val bitmap = BitmapManager.overlay(
					markerBitmap,
					whiteCircleBitmap,
					RemoteImages.AVATAR_22.load(),
				)
				item.setMarker(
					BitmapDrawable(
						resources, Bitmap.createScaledBitmap(bitmap, 160, 160, true)
					)
				)
			}
		}
		this.markers = markers
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