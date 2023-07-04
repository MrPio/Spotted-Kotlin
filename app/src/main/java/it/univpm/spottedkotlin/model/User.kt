package it.univpm.spottedkotlin.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.enums.RemoteImages
import java.util.*

@IgnoreExtraProperties
data class User(
	val name: String? = null,
	val surname: String? = null,
	var avatar: String = RemoteImages.ANONNYMOUS.url,
	val regDate: Date = Calendar.getInstance().time,
	val gender: Gender? = null,
	val tags: MutableList<Tag> = mutableListOf(),
	val postsUIDs: MutableList<String> = mutableListOf(),
	val comments: MutableList<Comment> = mutableListOf(),
	val following: MutableList<String> = mutableListOf(),
	val cellNumber: String? = null,
	val instagramNickname: String? = null,
) {
	var uid: String? = null

	@Exclude @JvmField
	val posts: MutableList<Post> = mutableListOf()

	@Exclude @JvmField
	val followingPosts: MutableList<Post> = mutableListOf()
}