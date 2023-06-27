package it.univpm.spottedkotlin.viewmodel

import android.app.AlertDialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.univpm.spottedkotlin.managers.AccountManager
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

    fun logout(){


        AccountManager.logout()
    }
	// TODO: Implement the ViewModel
}