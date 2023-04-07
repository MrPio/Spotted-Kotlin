package it.univpm.spottedkotlin.model

import java.util.*

data class Comment(
	val authorUID: String? = null,
	val text: String = "",
	val date: Date = Calendar.getInstance().time,
) {
	var user: User? = null
}