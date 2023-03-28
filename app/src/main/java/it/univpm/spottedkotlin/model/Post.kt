package it.univpm.spottedkotlin.model

import com.google.firebase.database.IgnoreExtraProperties
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.enums.Locations
import it.univpm.spottedkotlin.extension.function.toDateStr
import it.univpm.spottedkotlin.extension.function.toTimeStr
import java.util.*

@IgnoreExtraProperties
data class Post(
	val authorUID: String? = null,
	val location: Locations,
	val gender: Gender = Gender.FEMALE,
	val date: Date = Calendar.getInstance().time,
	val description: String = "",
	val tags: MutableList<Tag>? = null,
	val followers: MutableList<String>? = null,
	val comments: MutableList<Comment>? = null,
) {
	var uid: String? = null
	var percentage: Int = 0

	fun dateStr()  = date.toDateStr()
	fun timeStr()  = date.toTimeStr()
	fun commentsCount()  = comments?.size ?: 0
	fun followersCount() = followers?.size ?: 0
}