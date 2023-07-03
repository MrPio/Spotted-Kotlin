package it.univpm.spottedkotlin.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.enums.Locations
import it.univpm.spottedkotlin.extension.function.toDateStr
import it.univpm.spottedkotlin.extension.function.toTimeStr
import it.univpm.spottedkotlin.interfaces.Validable
import java.io.Serializable
import java.util.*

@IgnoreExtraProperties
data class Post(
	var authorUID: String? = null,
	var location: Locations? = null,
	var gender: Gender = Gender.FEMALE,
	var timestamp: Long = Calendar.getInstance().time.time,
	var description: String = "",
	val tags: MutableList<Tag> = mutableListOf(),
	val followers: MutableList<String> = mutableListOf(),
	val comments: MutableList<Comment> = mutableListOf(),
	var latitude: Double? = null,
	var longitude: Double? = null,
	var anonymous: Boolean = false,
	var spotted: Boolean = false,
) : Serializable, Validable {

	@Exclude @JvmField
	var uid: String? = null

	@Exclude @JvmField
	var author: User? = null

	@Exclude @JvmField
	var lastFollowers: MutableList<User?> = mutableListOf()

	@get:Exclude
	val date: Date get() = Date(timestamp)

	fun dateStr() = date.toDateStr()
	fun timeStr() = date.toTimeStr()
	fun calculateRelevance(tags: List<Tag>): Int =
		if (tags.isEmpty())
			0
		else
			(tags.count { tag -> this.tags.any { tag == it } } * 100f / tags.size).toInt()

	override fun validate(): List<String> {
		val errors = mutableListOf<String>()
		if (location == null)
			errors.add("Il luogo del post deve essere specificato.")
		if (description.length < 6 || description.length > 1000)
			errors.add("La descrizione deve essere presente e non più lunga di 1000 caratteri.")
		if (tags.size < 3)
			errors.add("Specifica almeno 3 tags")
		return errors.toList()
	}
}