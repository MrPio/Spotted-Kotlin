package it.univpm.spottedkotlin.managers

import android.content.Context
import android.content.SharedPreferences

object IOManager {
	private const val defaultFile = "settings"
	private lateinit var sharedPreferences: SharedPreferences

	fun initialize(context: Context, file: String = defaultFile) {
		sharedPreferences = context.getSharedPreferences(file, Context.MODE_PRIVATE)
	}

	fun writeKey(key: String, value: Any?) =
		when (value) {
			is String -> sharedPreferences.edit().putString(key, value).apply()
			is Boolean -> sharedPreferences.edit().putBoolean(key, value).apply()
			is Int -> sharedPreferences.edit().putInt(key, value).apply()
			is Float -> sharedPreferences.edit().putFloat(key, value).apply()
			is Long -> sharedPreferences.edit().putLong(key, value).apply()
			else -> {}
		}

	fun readKey(key: String): Any? =
		sharedPreferences.all[key]

	fun removeKey(key: String) =
		sharedPreferences.edit().remove(key).apply()
}