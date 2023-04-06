package it.univpm.spottedkotlin.model

import com.google.firebase.database.IgnoreExtraProperties
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.enums.Locations
import it.univpm.spottedkotlin.extension.function.toDateStr
import it.univpm.spottedkotlin.extension.function.toTimeStr
import java.io.Serializable
import java.util.*

@IgnoreExtraProperties
data class Post(
	var authorUID: String? = null,
	var location: Locations? = null,
	var gender: Gender = Gender.FEMALE,
	var date: Date = Calendar.getInstance().time,
	var description: String = "",
	val tags: MutableList<Tag> = mutableListOf(),
	val followers: MutableList<String> = mutableListOf(),
	val comments: MutableList<Comment> = mutableListOf(),
) : Serializable {
	var uid: String? = null
	var percentage: Int? = null

	fun dateStr() = date.toDateStr()
	fun timeStr() = date.toTimeStr()
	fun commentsCount() = comments?.size ?: 0
	fun followersCount() = followers?.size ?: 0
}