package it.univpm.spottedkotlin.extension.function

fun Boolean.toInt(): Int = if (this) 1 else 0

infix fun <T> Boolean.then(trueValue: T): T? = if (this) trueValue else null
