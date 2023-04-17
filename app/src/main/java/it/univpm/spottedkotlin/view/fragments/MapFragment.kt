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
import it.univpm.spottedkotlin.extension.MyMapView
import it.univpm.spottedkotlin.extension.function.checkAndAskPermission
import it.univpm.spottedkotlin.extension.function.loadDrawable
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
	private lateinit var map: MyMapView
	private lateinit var mapController: IMapController

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		requireActivity().checkAndAskPermission(WRITE_EXTERNAL_STORAGE, ACCESS_FINE_LOCATION)
		Configuration.getInstance()
			.load(requireContext(), requireActivity().getPreferences(Context.MODE_PRIVATE))
		binding = MapFragmentBinding.inflate(inflater, container, false)

		map = binding.mapMap
		map.setTileSource(TileSourceFactory.MAPNIK)
		map.setBuiltInZoomControls(false)
		map.setMultiTouchControls(true)

		map.minZoomLevel = 13.0
		map.maxZoomLevel = 16.0
		mapController = map.controller
		mapController.setZoom(13.0)
		val startPoint = GeoPoint(43.6100, 13.5134)

		val items = ItemizedIconOverlay(
			requireContext(), loadMarkers(), object : OnItemGestureListener<OverlayItem> {
				override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean {
					Toast.makeText(context, "$index", Toast.LENGTH_SHORT).show()
					return true
				}

				override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean = true
			}
		)

		val startZoom = 14.0
		map.overlays.add(items)
		mapController.setCenter(startPoint)
		mapController.animateTo(startPoint, startZoom, 1200)
		val startTime = System.currentTimeMillis()
		map.overlays.add(MyLocationNewOverlay(map))

		map.setOnPanAndZoomListener(object : OnPanAndZoomListener {
			override fun onDraw(geo: GeoPoint) {
				if (System.currentTimeMillis() - startTime < 2000)
					return
				val halfWidth = map.longitudeSpanDouble / 2
				val left = geo.longitude - map.longitudeSpanDouble / 2
				val right = geo.longitude + map.longitudeSpanDouble / 2
				val minLeft = startPoint.longitude - 0.12 / 2
				val maxRight = startPoint.longitude + 0.12 / 2

				if (left < minLeft) {
					mapController.stopAnimation(false)
					mapController.animateTo(
						GeoPoint(
							geo.latitude,
							minLeft + halfWidth
						), map.zoomLevelDouble, 250
					)
				} else if (right > maxRight) {
					mapController.stopAnimation(false)
					mapController.animateTo(
						GeoPoint(
							geo.latitude,
							maxRight - halfWidth
						), map.zoomLevelDouble, 250
					)
				}
			}

			override fun onPan(geo: GeoPoint) {
			}

			override fun onZoom(zoom: Int) {
			}
		})

		return binding.root
	}

	private fun loadMarkers(): MutableList<OverlayItem> {
		val markers = mutableListOf<OverlayItem>()
		for (i in 0..9) {
			markers.add(
				OverlayItem(
					"Tizio",
					"Tizio",
					GeoPoint(43.6100 + Math.random() * 0.025, 13.5134 + Math.random() * 0.025)
				).apply {
					val marker =
						requireContext().loadDrawable(R.drawable.map_marker) as BitmapDrawable
					setMarker(
						BitmapDrawable(
							resources, Bitmap.createScaledBitmap(marker.bitmap, 55, 55, true)
						)
					)

					thread {
						val bitmap = BitmapManager.overlay(
							RemoteImages.MAP_MARKER.load(),
							RemoteImages.CIRCLE_WHITE.load(),
							RemoteImages.AVATAR_23.load(),
						)
						setMarker(
							BitmapDrawable(
								resources,
								Bitmap.createScaledBitmap(bitmap, 160, 160, true)
							)
						)
					}
				})
		}
		return markers
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