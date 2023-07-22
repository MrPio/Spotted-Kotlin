package it.univpm.spottedkotlin.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import it.univpm.spottedkotlin.managers.AccountManager
import java.io.Serializable
import java.util.*

@IgnoreExtraProperties
data class Comment(
	val authorUID: String? = null,
	val text: String = "",
	val timestamp: Long = Calendar.getInstance().time.time,
	var receivedTimestamp: Long? = null,
	var readTimestamp: Long? = null,
) : Serializable {

	@Exclude
	@JvmField
	var user: User? = null

	@get:Exclude
	val date: Date get() = Date(timestamp)

	@get:Exclude
	val isMine: Boolean get() = authorUID == AccountManager.user.uid
}