package it.univpm.spottedkotlin.viewmodel

import android.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.univpm.spottedkotlin.managers.AccountManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    lateinit var gotoFirstActivityCallback: () -> Unit

    fun logout(){
        AccountManager.logout()
        gotoFirstActivityCallback.invoke()
    }
	// TODO: Implement the ViewModel
}