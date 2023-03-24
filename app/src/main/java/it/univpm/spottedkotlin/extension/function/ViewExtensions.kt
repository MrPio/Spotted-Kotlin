package it.univpm.spottedkotlin.extension.function

import android.view.View

fun View.rawPos(): IntArray {
	val point = IntArray(2)
	this.getLocationOnScreen(point)
	return point
}

fun View.rawX(): Float = this.rawPos()[0].toFloat()
fun View.rawY(): Float = this.rawPos()[1].toFloat()

fun View.setWidth(width: Int): View {
	this.layoutParams = layoutParams.apply { this.width = width }
	return this
}

fun View.setHeight(height: Int): View {
	this.layoutParams = layoutParams.apply { this.height = height }
	return this
}
