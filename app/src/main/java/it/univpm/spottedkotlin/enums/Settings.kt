package it.univpm.spottedkotlin.enums

import it.univpm.spottedkotlin.managers.IOManager
import it.univpm.spottedkotlin.model.SettingType
import java.util.*

enum class Settings(
	private val _id: String? = null,
	private val default: Any? = null,
) {
	FILTER_SPOTTED,
	FILTER_MINE,

	CHAT_OBSERVE(default = true),
	CHAT_EMOJI(default = true),
	CHAT_TIME(default = true),

	MAP_BOUNDARY(default = true),
	MAP_MARKERS_BIG;

	val id get() = _id ?: name.uppercase()
	val value get() = IOManager.readKey(id)
	val bool get() = (value ?: default ?: false) as Boolean
	val string get() = (value as? String) ?: (default as? String)
}