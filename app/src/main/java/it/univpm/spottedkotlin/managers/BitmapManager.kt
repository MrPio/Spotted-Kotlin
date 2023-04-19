package it.univpm.spottedkotlin.managers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import androidx.core.graphics.scale
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.extension.function.loadUrl
import kotlin.concurrent.thread


object BitmapManager {
	fun load(img: RemoteImages): Bitmap =
		try {
			Picasso.get().load(img.url).networkPolicy(NetworkPolicy.OFFLINE).get()
		} catch (e: Exception) {
			Picasso.get().load(img.url).get()
		}

	fun overlay(marker: Bitmap, circle: Bitmap, avatar: Bitmap? = null): Bitmap {
		val bmOverlay = Bitmap.createBitmap(marker.width, marker.height, marker.config)
		val canvas = Canvas(bmOverlay)
		canvas.drawBitmap(marker, Matrix(), null)
		canvas.drawBitmap(
			circle.scale((circle.width * 0.55).toInt(), (circle.height * 0.55).toInt()),
			circle.width * 0.45f / 2f,
			40f,
			null
		)
		if (avatar != null)
			canvas.drawBitmap(
				avatar.scale((avatar.width * 0.65).toInt(), (avatar.height * 0.75).toInt()),
				avatar.width * 0.35f / 2f,
				-30f,
				null
			)
		return bmOverlay
	}

	fun overlay(marker: Bitmap, circle: Bitmap, ten: Bitmap?, unit: Bitmap): Bitmap {
		val bitmap = overlay(marker, circle)
		val canvas = Canvas(bitmap)
		if (ten != null) {
			canvas.drawBitmap(
				ten.scale((ten.width * 0.21).toInt(), (ten.height * 0.21).toInt()),
				35f,
				25f,
				null
			)
		}
		canvas.drawBitmap(
			unit.scale((unit.width * 0.21).toInt(), (unit.height * 0.21).toInt()),
			if (ten != null)circle.width / 4 + 35f else 108f,
			25f,
			null
		)
		return bitmap
	}
}