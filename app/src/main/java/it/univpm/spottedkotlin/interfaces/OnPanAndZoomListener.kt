package it.univpm.spottedkotlin.interfaces

import org.osmdroid.util.GeoPoint

interface OnPanAndZoomListener {
	fun onPan(geo:GeoPoint)
	fun onZoom(zoom:Int)

	fun onDraw(geo:GeoPoint)
}