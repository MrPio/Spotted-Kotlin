package it.univpm.spottedkotlin.extension.function

import android.app.Activity
import android.content.Intent
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
	if (!this.applicationContext.checkPermission(*permissions)) this.askPermission(*permissions) else null

inline fun <reified T> Activity.goto(arguments: Map<String, String?>?=null) {
	this.startActivity(Intent(this, T::class.java).apply {
		arguments?.forEach {
			putExtra(
				it.key,
				it.value
			)
		}
	})
}