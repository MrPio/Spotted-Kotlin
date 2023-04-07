package it.univpm.spottedkotlin.model

import com.google.firebase.database.IgnoreExtraProperties
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.enums.RemoteImages
import java.util.*

@IgnoreExtraProperties
data class User(
	val name: String? = null,
	val surname: String? = null,
	val avatar: String = RemoteImages.ANONNYMOUS.url,
	val regDate: Date = Calendar.getInstance().time,
	val gender: Gender? = null,
	val tags: MutableList<Tag> = mutableListOf(),
	val posts: MutableList<String> = mutableListOf(),
	val comments: MutableList<Comment> = mutableListOf(),
	val following: MutableList<String> = mutableListOf(),
	val cellNumber: String? = null,
	val instagramNickname: String? = null,
) {
	var uid: String? = null
}