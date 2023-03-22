package it.univpm.spottedkotlin.extension

import android.content.Context
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun Context.load(res: Int) = this.resources.getString(res)
fun LocalDateTime.toDateStr()=this.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
fun LocalDateTime.toTimeStr()=this.format(DateTimeFormatter.ofPattern("HH:mm"))