package it.univpm.spottedkotlin.extension

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.OverlayItem

class MultiOverlayItem(
	title: String,
	snippet: String,
	val markers: MutableSet<OverlayItem>,
	val drawable: BitmapDrawable
) :
	OverlayItem(
		title,
		snippet,
		GeoPoint(markers.sumOf { it.point.latitude } / markers.size,
			markers.sumOf { it.point.longitude } / markers.size
		)
	) {

	init {
		setMarker(drawable)
	}
}