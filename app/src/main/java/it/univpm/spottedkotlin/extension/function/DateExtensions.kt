package it.univpm.spottedkotlin.extension.function

import java.text.SimpleDateFormat
import java.util.*

private val now get() = Calendar.getInstance().time.time
fun Date.toDateStr(day: Boolean = true): String =
	SimpleDateFormat(if (day) "dd MMM yyyy" else "MMM yyyy", Locale.getDefault()).format(this)

fun Date.toShortDateStr(): String = SimpleDateFormat("dd MMM", Locale.getDefault()).format(this)
fun Date.toTimeStr(): String = SimpleDateFormat("HH:mm", Locale.getDefault()).format(this)
fun Date.toConceptualStr(): String =
	if (now - this.time < 1000 * 3600 * 24)
		this.toTimeStr()
	else
		this.toDateStr()

fun Date.toPostStr(): String =
	if (now - this.time < 1000 * 60)
		"alcuni sec fa"
	else if (now - this.time < 1000 * 3600)
		"${((now - this.time) / 1000 / 60).toInt()} min fa"
	else if (now - this.time < 1000 * 3600 * 23)
		"${((now - this.time) / 1000 / 3600).toInt()} ore fa"
	else if (now - this.time < 1000 * 3600 * 24 * 6)
		SimpleDateFormat("EEEE", Locale.getDefault()).format(this)
	else if (now - this.time < 1000L * 3600 * 24 * 364)
		this.toShortDateStr()
	else this.toDateStr()

fun Date.addDays(days: Int): Date {
	val cal = Calendar.getInstance()
	cal.time = this
	cal.add(Calendar.DATE, days) //minus number would decrement the days
	return cal.time
}