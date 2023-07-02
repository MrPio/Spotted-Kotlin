package it.univpm.spottedkotlin.model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

enum class SettingType(val type: Class<*>) {
	FLAG(Boolean::class.java)
}

@IgnoreExtraProperties
data class SettingItem(
	val id: String = "",
	val title: String = "",
	val subtitle: String = "",
	val type: SettingType = SettingType.FLAG,
//	val section: String? = null, // Display section title
//	val isLast: Boolean = false, // Display bottom HLine
) : Serializable {}