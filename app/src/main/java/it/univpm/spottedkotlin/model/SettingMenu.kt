package it.univpm.spottedkotlin.model

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class SettingMenu(
	val icon: String = "",
	val title: String = "",
	val subtitle: String = "",
	var onClick: () -> Unit = {},
	val items: List<SettingItem>? = null,
) : Serializable {}