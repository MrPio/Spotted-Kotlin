package it.univpm.spottedkotlin.managers

import android.graphics.Bitmap
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
//		Picasso.get().load(img.url).get()
		Picasso.get().load(img.url).noFade().noPlaceholder().get()

	fun overlay(marker: Bitmap, circle: Bitmap, avatar: Bitmap): Bitmap {
		val bmOverlay = Bitmap.createBitmap(marker.width, marker.height, marker.config)
		val canvas = Canvas(bmOverlay)
		canvas.drawBitmap(marker, Matrix(), null)
		canvas.drawBitmap(
			circle.scale((circle.width * 0.54).toInt(), (circle.height * 0.65).toInt()),
			circle.width * 0.46f / 2f,
			30f,
			null
		)
		canvas.drawBitmap(
			avatar.scale((avatar.width * 0.65).toInt(), (avatar.height * 0.75).toInt()),
			avatar.width * 0.35f / 2f,
			-30f,
			null
		)
		return bmOverlay
	}
}