package it.univpm.spottedkotlin.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.enums.Tags
import it.univpm.spottedkotlin.managers.AccountManager
import java.util.*

@IgnoreExtraProperties
data class User(
	val name: String? = null,
	val surname: String? = null,
	var avatar: String = RemoteImages.ANONYMOUS.url,
	val regDateTimestamp: Long = Calendar.getInstance().time.time,
	val gender: Gender? = null,
	val tags: MutableList<Tags> = mutableListOf(),
	val postsUIDs: MutableList<String> = mutableListOf(),
	val comments: MutableList<Comment> = mutableListOf(),
	val following: MutableList<String> = mutableListOf(),
	val followingUsers: MutableList<String> = mutableListOf(),
	val chatsUserUID: MutableList<String> = mutableListOf(),
	val cellNumber: String? = null,
	val instagramNickname: String? = null,
) {
	@Exclude
	@JvmField
	var uid: String? = null

	@get:Exclude
	val regDate: Date get() = Date(regDateTimestamp)

	@Exclude
	@JvmField
	val posts: MutableList<Post> = mutableListOf()

	@Exclude
	@JvmField
	val followingPosts: MutableList<Post> = mutableListOf()

	@Exclude
	@JvmField
	val chats: MutableList<Chat> = mutableListOf()

	@Exclude
	fun isFollowing(): Boolean =
		AccountManager.isUserInitialized && AccountManager.user.followingUsers.contains(this.uid)

	@Exclude
	fun isMe(): Boolean = AccountManager.isUserInitialized && AccountManager.user.uid == this.uid

	@Exclude
	fun hasChatWithMe(): Boolean =
		AccountManager.isUserInitialized && AccountManager.user.uid != null && this.chatsUserUID.contains(
			AccountManager.user.uid!!
		)
}

