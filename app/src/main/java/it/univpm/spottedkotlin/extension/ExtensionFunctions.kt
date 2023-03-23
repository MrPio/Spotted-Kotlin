package it.univpm.spottedkotlin.extension

import android.content.Context
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun Context.loadStr(res: Int) = this.resources.getString(res)
fun Context.loadColor(res: Int) = this.resources.getColor(res,theme)
fun LocalDateTime.toDateStr():String=this.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
fun LocalDateTime.toTimeStr():String=this.format(DateTimeFormatter.ofPattern("HH:mm"))