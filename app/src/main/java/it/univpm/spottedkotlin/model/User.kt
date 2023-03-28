package it.univpm.spottedkotlin.model

import com.google.firebase.database.IgnoreExtraProperties
import it.univpm.spottedkotlin.enums.Gender
import java.util.*

@IgnoreExtraProperties
data class User(
	val name: String? = null,
	val surname: String? = null,
	val regDate: Date = Calendar.getInstance().time,
	val gender: Gender? = null,
	val tags: MutableList<Tag>? = null,
	val posts: MutableList<String>? = null,
	val comments: MutableList<Comment>? = null,
	val following: MutableList<String>? = null,
	val cellNumber: String? = null,
	val instagramNickname: String? = null,
){
	var uid: String? = null
}