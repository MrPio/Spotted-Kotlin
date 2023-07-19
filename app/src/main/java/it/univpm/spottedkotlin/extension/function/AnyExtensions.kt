package it.univpm.spottedkotlin.extension.function

import it.univpm.spottedkotlin.managers.LogManager

fun Any.log() = LogManager.log(this.toString())

//fun <T> Any?.check(x: T, y: T): T = if (this == null) x else y
