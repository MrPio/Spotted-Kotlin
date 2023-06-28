package it.univpm.spottedkotlin.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.provider.ContactsContract.Data
import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.extension.function.loadBitmapDrawable
import it.univpm.spottedkotlin.extension.function.log
import it.univpm.spottedkotlin.managers.BitmapManager
import it.univpm.spottedkotlin.managers.DataManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.OverlayItem
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class MapViewModel : ViewModel() {

	suspend fun loadOverlayItems(context: Context): List<OverlayItem> {
		DataManager.reloadPaginatedData()
		DataManager.loadMore(pageSize = 120)
		val overlayItems = mutableListOf<OverlayItem>()

		for (post in DataManager.posts) {

			// If there is no valid post location, continue
			if (post.location == null && (post.latitude == null || post.longitude == null))
				continue

			// Determine the marker geoPoint
			val randSpan = 0.0003
			val geoPoint =
				if (post.location != null)
					GeoPoint(
						post.location!!.latitude + Math.random() * randSpan,
						post.location!!.longitude + Math.random() * randSpan,
					)
				else
					GeoPoint(
						post.latitude!!,
						post.longitude!!,
					)

			val overlayItem = OverlayItem(
				post.uid,
				"Post di ${post.authorUID}",
				post.description,
				geoPoint
			)

			overlayItem.setMarker(context.loadBitmapDrawable(R.drawable.map_marker, 55, 55))
			overlayItems.add(overlayItem)
		}

		GlobalScope.launch {
			val whiteCircleBitmap = RemoteImages.CIRCLE_WHITE.load()
			val markerBitmap = RemoteImages.MAP_MARKER.load()
			for (overlayItem in overlayItems) {
				val authorUID = DataManager.posts.find { it.uid == overlayItem.uid }?.authorUID
				val avatarUrl = DataManager.loadUser(authorUID).avatar
				val bitmap = BitmapManager.overlay(
					markerBitmap,
					whiteCircleBitmap,
					BitmapManager.load(avatarUrl),
				)
				overlayItem.setMarker(
					context.loadBitmapDrawable(bitmap, 136, 136)
				)
			}
		}
		return overlayItems
	}
}