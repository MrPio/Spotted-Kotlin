package it.univpm.spottedkotlin.extension.function

import it.univpm.spottedkotlin.managers.DeviceManager

fun Number.toDp(): Float = this.toFloat() / DeviceManager.displayMetrics.density
