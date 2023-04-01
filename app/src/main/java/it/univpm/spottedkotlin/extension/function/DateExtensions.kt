package it.univpm.spottedkotlin.extension.function

import java.text.SimpleDateFormat
import java.util.*

fun Date.toDateStr(): String = SimpleDateFormat("dd MMM yyyy").format(this)
fun Date.toTimeStr(): String = SimpleDateFormat("HH:mm").format(this)

fun Date.addDays(days: Int): Date {
	val cal = Calendar.getInstance()
	cal.time = this
	cal.add(Calendar.DATE, days) //minus number would decrement the days
	return cal.time
}