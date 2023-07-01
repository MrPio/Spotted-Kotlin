package it.univpm.spottedkotlin.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.managers.AccountManager

class SettingsViewModel : ViewModel() {
    lateinit var gotoFirstActivityCallback: () -> Unit

    fun logout(){
        AccountManager.logout()
        gotoFirstActivityCallback.invoke()
    }
}