package it.univpm.spottedkotlin.viewmodel

import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.model.SettingMenu

class SettingsViewModel : ViewModel() {
	lateinit var gotoFirstActivityCallback: () -> Unit

	val settingMenus: List<SettingMenu> get() = DataManager.settingMenus

	fun logout() {
		AccountManager.logout()
		gotoFirstActivityCallback()
	}
}