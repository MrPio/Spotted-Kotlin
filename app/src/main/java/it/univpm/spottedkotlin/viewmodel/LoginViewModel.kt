package it.univpm.spottedkotlin.viewmodel

import android.content.Intent
import android.service.voice.VoiceInteractionSession.VisibleActivityCallback
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.view.MainActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel() {
    var email:String ="admin@admin.com"
    var password : String ="admin%"
    lateinit var goToMainActivityCallback : ()->Unit

    fun login(){
        try {
            MainScope().launch {
                AccountManager.login(email,password)
                goToMainActivityCallback()
            }


        }
        catch (/*mia eccezione*/ e:Exception){}

    }
}