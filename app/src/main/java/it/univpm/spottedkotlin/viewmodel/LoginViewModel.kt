package it.univpm.spottedkotlin.viewmodel


import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.managers.AccountManager
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
        catch (/*mia eccezione*/ _:Exception){}
    }
}