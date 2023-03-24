package it.univpm.spottedkotlin.extension

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.DisplayMetrics
import android.view.View
import it.univpm.spottedkotlin.managers.DeviceManager
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun Context.loadStr(res: Int) = this.resources.getString(res)
fun Context.loadColor(res: Int) = this.resources.getColor(res, theme)

inline fun <reified T> Context.getActivity(): T? {
	var context = this
	while (context is ContextWrapper) {
		if (context is T)
			return context
		context = context.baseContext
	}
	return null
}

fun Activity.metrics(): DisplayMetrics {
	val displayMetrics = DisplayMetrics()
	this.windowManager.defaultDisplay.getMetrics(displayMetrics)
	return displayMetrics
}

fun Activity.screenWidth(): Int = this.metrics().widthPixels
fun Activity.screenHeight(): Int = this.metrics().heightPixels
fun Number.toDp(): Float = this.toFloat() / DeviceManager.displayMetrics.density
fun LocalDateTime.toDateStr(): String = this.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
fun LocalDateTime.toTimeStr(): String = this.format(DateTimeFormatter.ofPattern("HH:mm"))

fun View.rawPos(): IntArray {
	val point = IntArray(2)
	this.getLocationOnScreen(point)
	return point
}

fun View.rawX(): Float = this.rawPos()[0].toFloat()
fun View.rawY(): Float = this.rawPos()[1].toFloat()