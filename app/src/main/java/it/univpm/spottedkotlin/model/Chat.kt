package it.univpm.spottedkotlin.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import it.univpm.spottedkotlin.extension.function.toConceptualStr
import it.univpm.spottedkotlin.extension.function.toPostStr
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
	var users: MutableList<User> = mutableListOf()

	@get:Exclude
	val uid: String get() = authors.sorted().joinToString("_")

	@get:Exclude
	val date: Date get() = Date(timestamp)

	@get:Exclude
	val lastSeen: String get() = date.toPostStr()

	@get:Exclude
	val hasNewMessages: Boolean get() = messages.find { msg -> !msg.isMine && msg.receivedTimestamp == null } != null
}