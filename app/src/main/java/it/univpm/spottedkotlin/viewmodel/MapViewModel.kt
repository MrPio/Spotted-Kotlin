package it.univpm.spottedkotlin.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.extension.function.loadBitmapDrawable
import it.univpm.spottedkotlin.managers.BitmapManager
import it.univpm.spottedkotlin.managers.DataManager
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.OverlayItem
import kotlin.concurrent.thread

class MapViewModel : ViewModel() {

	fun loadOverlayItems(context: Context): List<OverlayItem> {
		val posts = DataManager.posts
		val overlayItems = mutableListOf<OverlayItem>()

		for (post in posts) {
			val overlayItem = OverlayItem(
				"uid",
				"title",
				"description",
				GeoPoint(43.6100 + Math.random() * 0.025, 13.5134 + Math.random() * 0.025)
			)

			overlayItem.setMarker(context.loadBitmapDrawable(R.drawable.map_marker, 55, 55))
			overlayItems.add(overlayItem)
		}

		thread {
			val whiteCircleBitmap = RemoteImages.CIRCLE_WHITE.load()
			val markerBitmap = RemoteImages.MAP_MARKER.load()
			for (overlayItem in overlayItems) {
				val bitmap = BitmapManager.overlay(
					markerBitmap,
					whiteCircleBitmap,
					RemoteImages.AVATAR_22.load(),
				)
				overlayItem.setMarker(
					context.loadBitmapDrawable(bitmap, 160, 160)
				)
			}
		}

		return overlayItems
	}
}