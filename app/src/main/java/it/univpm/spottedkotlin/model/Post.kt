package it.univpm.spottedkotlin.model

import com.google.firebase.database.IgnoreExtraProperties
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.enums.Locations
import it.univpm.spottedkotlin.extension.function.toDateStr
import it.univpm.spottedkotlin.extension.function.toTimeStr
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

@IgnoreExtraProperties
data class Post(
	val authorUID: String? = null,
	val followers: List<String>? = null,
	val comments: List<Comment>? = null,
	val locationImage: String = Locations.QT_140.imageUrl,
	val percentage: Int = 0,
	val location: String = Locations.QT_140.title,
	val date: Date = Calendar.getInstance().time,
	val description: String = "",
	val tags: List<Tag>? = null,
	val gender: Gender = Gender.FEMALE,
) {
	var uid: String? = null

	fun dateStr()  = date.toDateStr()
	fun timeStr()  = date.toTimeStr()
	fun commentsCount()  = comments?.size ?: 0
	fun followersCount() = followers?.size ?: 0
}