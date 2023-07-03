package it.univpm.spottedkotlin.extension.function

import android.app.Activity
import android.util.DisplayMetrics
import androidx.core.app.ActivityCompat

fun Activity.metrics(): DisplayMetrics {
	val displayMetrics = DisplayMetrics()
	this.windowManager.defaultDisplay.getMetrics(displayMetrics)
	return displayMetrics
}

fun Activity.screenWidth(): Int = this.metrics().widthPixels
fun Activity.screenHeight(): Int = this.metrics().heightPixels

fun Activity.askPermission(vararg permissions: String) =
	ActivityCompat.requestPermissions(this, permissions, 1)

fun Activity.checkAndAskPermission(vararg permissions: String) =
	if (!this.applicationContext.checkPermission(*permissions))
		this.askPermission(*permissions) else null