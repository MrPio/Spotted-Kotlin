package it.univpm.spottedkotlin.model

import android.content.Context
import com.google.firebase.database.IgnoreExtraProperties
import it.univpm.spottedkotlin.enums.Settings
import java.io.Serializable

enum class SettingType() {
	FLAG,
	SLIDER,
	ALERT_YES_NO,
	ALERT_OK,
	ACTION,
}

@IgnoreExtraProperties
data class SettingItem(
	val id: String? = null,
	val title: String = "",
	val subtitle: String = "",
	val type: SettingType = SettingType.FLAG,
	val valueFrom: Int = 0,
	val valueTo: Int = 100,
	val alertMessage: String = "",
	val action: (context: Context) -> Unit = {},
//	val section: String? = null, // Display section title
//	val isLast: Boolean = false, // Display bottom HLine
) : Serializable {
	fun setting() = if (id == null) null else Settings.valueOf(id.uppercase())
}