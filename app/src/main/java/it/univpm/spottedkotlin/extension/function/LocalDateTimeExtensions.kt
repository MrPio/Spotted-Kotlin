package it.univpm.spottedkotlin.extension.function

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun LocalDateTime.toDateStr(): String = this.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
fun LocalDateTime.toTimeStr(): String = this.format(DateTimeFormatter.ofPattern("HH:mm"))
