package it.univpm.spottedkotlin.extension

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintSet.Motion
import it.univpm.spottedkotlin.interfaces.OnPanAndZoomListener
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import java.util.Calendar

class MyMapView(context: Context?, attrs: AttributeSet?) : MapView(context, attrs) {
	private var oldZoomLevel = -1
	private var oldPos = GeoPoint(0.0, 0.0)
	private var oldCenterGeoPoint: GeoPoint? = null
	private var mListener: OnPanAndZoomListener? = null
	private var lastDrawCall = System.currentTimeMillis()
	private var down = false
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
		if (System.currentTimeMillis() - lastDrawCall > 100) {
			lastDrawCall = Calendar.getInstance().time.time
			if (!down &&oldPos.latitude != center.latitude ) {
				mListener?.onDraw(center)
				oldPos = center
			}
			if (zoomLevel != oldZoomLevel) {
				mListener?.onZoom(zoomLevelDouble.toInt())
				oldZoomLevel = zoomLevel
			}
		}
	}
}