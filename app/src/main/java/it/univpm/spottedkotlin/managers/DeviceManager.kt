package it.univpm.spottedkotlin.managers

import android.content.Context
import android.content.res.Configuration
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatDelegate
import it.univpm.spottedkotlin.enums.Settings

object DeviceManager {

	// Load the preferred app theme
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

	// Scale the app UI to the given density
	fun loadUiDensity(context: Context) {
		val percentage = listOf(.85, .9, .95, 1.0, 1.05, 1.1)[Settings.APPEARANCE_SCALE_UI.int ?: 3]
		val newDensity = (DisplayMetrics.DENSITY_DEVICE_STABLE * percentage * 0.9).toInt()

		val configuration = Configuration()
		configuration.densityDpi = newDensity

		val displayMetrics = DisplayMetrics()
		displayMetrics.densityDpi = newDensity

		// Apply the scaled configuration and display metrics
		context.resources.updateConfiguration(configuration, displayMetrics)
	}

	lateinit var displayMetrics: DisplayMetrics
}