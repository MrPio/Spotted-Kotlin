package it.univpm.spottedkotlin.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable
import java.util.*

@IgnoreExtraProperties
data class Chat(
	val authors: MutableList<String> = mutableListOf(),
	val timestamp: Long = Calendar.getInstance().time.time,
	val messages: MutableList<Comment> = mutableListOf()
) : Serializable {

	@Exclude
	@JvmField
	var users: List<User>? = null

	@Exclude
	@JvmField
	var uid: String? = null

	@get:Exclude
	val date: Date get() = Date(timestamp)
}