package it.univpm.spottedkotlin.extension

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.Toast
import it.univpm.spottedkotlin.interfaces.OnPanAndZoomListener
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem

class MyMapView(context: Context?, attrs: AttributeSet?) : MapView(context, attrs) {
	private var oldZoomLevel = -1
	private var oldPos = GeoPoint(0.0, 0.0)
	private var oldCenterGeoPoint: GeoPoint? = null
	private var mListener: OnPanAndZoomListener? = null
	private var lastDrawCall = System.currentTimeMillis()
	private var down = false
	var markers: ItemizedIconOverlay<OverlayItem>? = null
		set(value) {
			field = value
			overlays.clear()
			overlays.add(markers)
		}
	//PASSARE A MUTABLE SET
	var multiMarkers: ItemizedIconOverlay<OverlayItem>? = null
		set(value) {
			field = value
			overlays.clear()
			overlays.add(markers)
		}

	fun loadMarkers(context: Context?, markers: List<OverlayItem>, onTap: ((Int) -> Unit)? = null) {
		this.markers = ItemizedIconOverlay(context, markers,
			object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
				override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean {
					onTap?.invoke(index)
					return true
				}

				override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean = true
			})
	}

	fun setOnPanAndZoomListener(listener: OnPanAndZoomListener?) {
		mListener = listener
	}

	override fun onTouchEvent(ev: MotionEvent): Boolean {
		if (ev.action == MotionEvent.ACTION_DOWN)
			down = true
		if (ev.action == MotionEvent.ACTION_UP) {
			down = false
			val centerGeoPoint: GeoPoint = this.mapCenter as GeoPoint
			if (oldCenterGeoPoint == null || oldCenterGeoPoint!!.latitudeE6 != centerGeoPoint.latitudeE6 || oldCenterGeoPoint!!.longitudeE6 != centerGeoPoint.longitudeE6) {
				mListener?.onPan(centerGeoPoint)
			}
			oldCenterGeoPoint = this.mapCenter as GeoPoint
		}
		return super.onTouchEvent(ev)
	}

	override fun dispatchDraw(canvas: Canvas) {
		super.dispatchDraw(canvas)
		val center = this.mapCenter as GeoPoint
		if (System.currentTimeMillis() - lastDrawCall > 200) {
			lastDrawCall = System.currentTimeMillis()
			//if (!down /*&&oldPos.latitude != center.latitude*/ ) {
			mListener?.onDraw(center)
			//oldPos = center
			//}
			if (zoomLevel != oldZoomLevel) {
				mListener?.onZoom(zoomLevelDouble.toInt())
				oldZoomLevel = zoomLevel
			}
		}
	}
}