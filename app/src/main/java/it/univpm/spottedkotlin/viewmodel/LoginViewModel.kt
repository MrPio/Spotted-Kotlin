package it.univpm.spottedkotlin.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.LogManager.TAG
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class LoginViewModel : ViewModel() {
    var email: String = ""
    var password: String = ""

    suspend fun login() {
        Log.e(TAG, email)
        if(!AccountManager.isEmailUsed(email)) throw Exception("E-mail o password errate")
        AccountManager.login(email, password)
    }


}