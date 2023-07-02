package it.univpm.spottedkotlin.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.enums.Settings
import it.univpm.spottedkotlin.extension.ObservableViewModel
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.managers.IOManager
import it.univpm.spottedkotlin.model.SettingItem
import it.univpm.spottedkotlin.model.SettingMenu

class SettingItemViewModel(val settingItem: SettingItem) : ObservableViewModel() {

	@get:Bindable
	var flag: Boolean = Settings.valueOf(settingItem.id.uppercase()).bool
		set(value) = IOManager.writeKey(settingItem.id, value)
}