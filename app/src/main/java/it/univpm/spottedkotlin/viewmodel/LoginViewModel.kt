package it.univpm.spottedkotlin.viewmodel


import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.managers.AccountManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class LoginViewModel : ViewModel() {
	var email: String = ""
	var password: String = ""
	lateinit var goToMainActivityCallback: () -> Unit

	fun login() {
		try {
			MainScope().launch {
				AccountManager.login(email, password)
				goToMainActivityCallback()
			}
		} catch (_: Exception) {
		}
	}
}