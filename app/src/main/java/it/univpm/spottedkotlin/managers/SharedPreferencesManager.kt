package it.univpm.spottedkotlin.managers

import android.content.Context



object SharedPreferencesManager {

	fun save(context: Context, valore: String) {
		val sharedPreferences = context.getSharedPreferences("utente", Context.MODE_PRIVATE)
		val editor = sharedPreferences.edit()
		editor.putString("uid", valore)
		editor.apply()
	}

	fun remove(context: Context) {
		val sharedPreferences = context.getSharedPreferences("utente", Context.MODE_PRIVATE)
		val editor = sharedPreferences.edit()
		editor.remove("uid")
		editor.apply()
	}

	fun read(context: Context, default: String= "none"): String {
		val sharedPreferences = context.getSharedPreferences("utente", Context.MODE_PRIVATE)
		return sharedPreferences.getString("uid", default) ?: default
	}


}