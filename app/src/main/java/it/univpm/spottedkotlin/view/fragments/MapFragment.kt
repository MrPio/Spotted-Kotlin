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
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

		map.loadMarkers(context,loadMarkers()){
			// MARKER - OnClickListener
			Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
		}

		val startZoom = 14.0
		mapController.setCenter(startPoint)
		mapController.animateTo(startPoint, startZoom, 1200)
		val startTime = System.currentTimeMillis()
		map.overlays.add(MyLocationNewOverlay(map))

		map.setOnPanAndZoomListener(object : OnPanAndZoomListener {
			override fun onDraw(geo: GeoPoint) {
				if (System.currentTimeMillis() - startTime < 2000) return
				val longitudeSpan = 0.12
				val latitudeSpan = 0.15

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
					Log.d("MY1","LEFT")
					mapController.animateTo(
						GeoPoint(
							geo.latitude, minLeft + halfWidth
						), map.zoomLevelDouble, 200
					)
				} else if (right > maxRight) {
					Log.d("MY1","RIGHT")
					mapController.animateTo(
						GeoPoint(
							geo.latitude, maxRight - halfWidth
						), map.zoomLevelDouble, 200
					)
				}

				// VERTICAL constraints
				if (bottom < minBottom) {
					Log.d("MY1","BOTTOM")
					mapController.animateTo(
						GeoPoint(
							minBottom + halfHeight, geo.longitude
						), map.zoomLevelDouble, 200
					)
				} else if (top > maxTop) {
					Log.d("MY1","TOP")
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
				val zooms= listOf(13,14,15,16)

			}
		})

		return binding.root
	}

	private fun loadMarkers(): MutableList<OverlayItem> {
		val markers = mutableListOf<OverlayItem>()
		//Il drawable del segnalino
		val marker = requireContext().loadDrawable(R.drawable.map_marker) as BitmapDrawable
		for (i in 0..49) {
			val item = OverlayItem(
				"Tizio",
				"Tizio",
				GeoPoint(43.6100 + Math.random() * 0.025, 13.5134 + Math.random() * 0.025)
			)

			// Setto il placeholder del segnalino
			item.setMarker(
				BitmapDrawable(
					resources, Bitmap.createScaledBitmap(marker.bitmap, 55, 55, true)
				)
			)
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