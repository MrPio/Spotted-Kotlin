package it.univpm.spottedkotlin.extension.function

import android.app.Activity
import android.util.DisplayMetrics

fun Activity.metrics(): DisplayMetrics {
	val displayMetrics = DisplayMetrics()
	this.windowManager.defaultDisplay.getMetrics(displayMetrics)
	return displayMetrics
}

fun Activity.screenWidth(): Int = this.metrics().widthPixels
fun Activity.screenHeight(): Int = this.metrics().heightPixels
