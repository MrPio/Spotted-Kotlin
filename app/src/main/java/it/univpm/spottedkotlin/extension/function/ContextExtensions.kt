package it.univpm.spottedkotlin.extension.function

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.PermissionChecker
import androidx.databinding.DataBindingUtil
import it.univpm.spottedkotlin.R

fun Context.loadColor(res: Int) = this.resources.getColor(res, theme)
fun Context.loadDrawable(res: Int) = AppCompatResources.getDrawable(this, res)
inline fun <reified T> Context.getActivity(): T? {
	var context = this
	while (context is ContextWrapper) {
		if (context is T)
			return context
		context = context.baseContext
	}
	return null
}

fun <T> Context.inflate(layout: Int, parent: ViewGroup? = null, attachToParent: Boolean = false) =
	DataBindingUtil.inflate(
		LayoutInflater.from(this),
		layout,
		parent,
		attachToParent
	) as T

fun Context.checkPermission(vararg permission: String): Boolean =
	permission.all {
		PermissionChecker.checkSelfPermission(this, it) == PermissionChecker.PERMISSION_GRANTED
	}

fun Context.loadBitmapDrawable(res: Int, width: Int, height: Int) =
	BitmapDrawable(
		this.resources,
		Bitmap.createScaledBitmap(
			(this.loadDrawable(res) as BitmapDrawable).bitmap,
			width,
			height,
			true
		)
	)

fun Context.loadBitmapDrawable(res: Bitmap, width: Int, height: Int) =
	BitmapDrawable(
		this.resources,
		Bitmap.createScaledBitmap(res, width, height, true)
	)