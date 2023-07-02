package it.univpm.spottedkotlin.managers

import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatDelegate
import it.univpm.spottedkotlin.enums.Settings

object DeviceManager {
	fun loadTheme() {
		Settings.APPEARANCE_THEME.int?.let {
			AppCompatDelegate.setDefaultNightMode(
				when (it) {
					1 -> AppCompatDelegate.MODE_NIGHT_NO
					2 -> AppCompatDelegate.MODE_NIGHT_YES
					else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
				}
			)
		}
	}

	lateinit var displayMetrics: DisplayMetrics
}