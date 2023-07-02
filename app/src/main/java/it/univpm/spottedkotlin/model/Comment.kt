package it.univpm.spottedkotlin.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable
import java.util.*

@IgnoreExtraProperties
data class Comment(
	val authorUID: String? = null,
	val text: String = "",
	val date: Date = Calendar.getInstance().time,
) : Serializable{

	@Exclude
	var user: User? = null
}