package it.univpm.spottedkotlin.extension.function

import it.univpm.spottedkotlin.managers.LogManager

fun String.log()=LogManager.log(this)