package it.univpm.spottedkotlin.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.SharedPreferencesManager

class SettingsViewModel : ViewModel() {
    lateinit var gotoFirstActivityCallback: () -> Unit

    fun logout(context: Context){
        AccountManager.logout(context)
        gotoFirstActivityCallback.invoke()
    }
	// TODO: Implement the ViewModel
}