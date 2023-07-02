package it.univpm.spottedkotlin.enums

import it.univpm.spottedkotlin.managers.IOManager

enum class Settings(
	private val _id: String? = null,
	private val default: Any? = null,
) {
	APPEARANCE_THEME(default = 0),

	FILTER_SPOTTED,
	FILTER_MINE,

	CHAT_OBSERVE(default = true),
	CHAT_EMOJI(default = true),
	CHAT_TIME(default = true),

	MAP_BOUNDARY(default = true),
	MAP_MARKERS_SIZE(default = 2);

	val id get() = _id ?: name.uppercase()
	var value
		get() = IOManager.readKey(id)
		set(value) = IOManager.writeKey(id, value)
	val bool get() = (value ?: default ?: false) as Boolean
	val string get() = (value as? String) ?: (default as? String)
	val int get() = (value as? Int) ?: (default as? Int)
}