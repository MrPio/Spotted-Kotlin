package it.univpm.spottedkotlin.extension.function

import java.text.SimpleDateFormat
import java.util.*

fun Date.toDateStr(): String = SimpleDateFormat("dd MMM yyyy").format(this)
fun Date.toTimeStr(): String = SimpleDateFormat("HH:mm").format(this)
