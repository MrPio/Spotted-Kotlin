package it.univpm.spottedkotlin.viewmodel

import android.content.Context
import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import com.google.android.material.slider.Slider
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.SliderPopupBinding
import it.univpm.spottedkotlin.enums.Settings
import it.univpm.spottedkotlin.extension.ObservableViewModel
import it.univpm.spottedkotlin.extension.function.inflate
import it.univpm.spottedkotlin.extension.function.showAlertDialog
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.managers.IOManager
import it.univpm.spottedkotlin.model.SettingItem
import it.univpm.spottedkotlin.model.SettingMenu
import it.univpm.spottedkotlin.model.SettingType
import java.lang.Integer.max

class SettingItemViewModel(val settingItem: SettingItem) : ObservableViewModel() {

	// SettingType.FLAG
	@get:Bindable
	var flag: Boolean =
		if (settingItem.type == SettingType.FLAG) settingItem.setting()!!.bool else false
		set(value) {
			settingItem.setting()?.value = value
			field = value
		}

	fun onClick(context: Context) {
		when (settingItem.type) {
			SettingType.SLIDER -> showSliderPopup(context)
			SettingType.ALERT_YES_NO -> showAlertYesNoPopup(context)
			SettingType.ALERT_OK -> showAlertOkPopup(context)
			SettingType.ACTION -> settingItem.action(context)
			SettingType.RADIO -> showRadioPopup(context)
			else -> {}
		}
	}

	// SettingType.SLIDER
	private fun showSliderPopup(context: Context) {
		val popupBinding = context.inflate<SliderPopupBinding>(R.layout.slider_popup)
		popupBinding.valueFrom = settingItem.valueFrom
		popupBinding.valueTo = settingItem.valueTo
		popupBinding.value = max(settingItem.valueFrom, settingItem.setting()!!.int ?: 0)

		context.showAlertDialog(
			title = settingItem.subtitle,
			view = popupBinding.root,
			positiveText = "Conferma",
			negativeText = "Annulla",
			negativeCallback = {},
			positiveCallback = { settingItem.setting()!!.value = popupBinding.value },
		)
	}

	// SettingType.ALERT_YES_NO
	private fun showAlertYesNoPopup(context: Context) {
		context.showAlertDialog(
			title = settingItem.title,
			message = settingItem.alertMessage,
			positiveText = "Conferma",
			negativeText = "Annulla",
			negativeCallback = {},
			positiveCallback = { settingItem.action(context) },
		)
	}

	// SettingType.ALERT_OK
	private fun showAlertOkPopup(context: Context) {
		context.showAlertDialog(
			title = settingItem.title,
			message = settingItem.alertMessage,
			positiveText = "Ok",
			positiveCallback = {},
		)
	}

	// SettingType.RADIO
	private fun showRadioPopup(context: Context) {
		val backupValue = settingItem.setting()?.int?:0
		context.showAlertDialog(
			title = settingItem.title,
			options = settingItem.options,
			checked = backupValue,
			radioCallback = { i -> settingItem.setting()?.value = i },
			positiveText = "Conferma",
			positiveCallback = { settingItem.action(context) },
			negativeText = "Annulla",
			negativeCallback = { settingItem.setting()?.value = backupValue }
		)
	}
}