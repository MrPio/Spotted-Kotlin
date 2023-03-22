package it.univpm.spottedkotlin.model

import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.extension.toDateStr
import it.univpm.spottedkotlin.extension.toTimeStr
import java.time.LocalDateTime

data class Post(
	val locationImage: String,
	val percentage: Int,
	val location: String = "Quota 140",
	val date: LocalDateTime = LocalDateTime.now(),
	val commentCount: Int = 6,
	val followCount: Int = 18,
	val tags: List<Tag>,
	val gender: Gender = Gender.FEMALE,
) {
	val dateStr: String get() = date.toDateStr()
	val timeStr: String get() = date.toTimeStr()
}