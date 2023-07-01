package it.univpm.spottedkotlin.managers

import android.content.Context
import android.content.SharedPreferences

object IOManager {
	private const val defaultFile = "settings"
	private lateinit var sharedPreferences: SharedPreferences

	fun initialize(context: Context, file: String = defaultFile) {
		sharedPreferences = context.getSharedPreferences(file, Context.MODE_PRIVATE)
	}

	fun writeKey(key: String, value: String) =
		sharedPreferences.edit().putString(key, value).apply()

	fun readKey(key: String): String? =
		sharedPreferences.getString(key, null)

	fun removeKey(key: String) =
		sharedPreferences.edit().remove(key).apply()
}