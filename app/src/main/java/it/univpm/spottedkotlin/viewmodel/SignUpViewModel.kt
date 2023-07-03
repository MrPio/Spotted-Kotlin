package it.univpm.spottedkotlin.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DatabaseManager
import it.univpm.spottedkotlin.managers.LogManager.TAG
import it.univpm.spottedkotlin.model.User
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class SignUpViewModel : ViewModel() {

	private val character: String = "abcdefghijklmnopqrstuvwxyz"
	private val character_up: String = character.uppercase()
	private val number: String = "0123456789"
	private val special: String = "!\"#\$€%&'()*+,-./:;<=>?@[\\]^_`{|}~"

	//Almeno 6 caratteri, al massimo 10 (ideali 8)
	private val MIN_LENGHT: Int = 6
	private val MAX_LENGHT: Int = 10

	var name: String = ""
	var surname: String = ""
	var instaUrl: String = ""
	var gender: Gender? = null
	var email: String = ""
	var password: String = ""
	var repeat_password: String = ""
	lateinit var goToMainActivityCallback: () -> Unit
	lateinit var goToSignUpFragmentCallback: () -> Unit


	fun setInfo() {
		AccountManager.user = User(name = name, surname = surname, instagramNickname = instaUrl, gender = gender)
		goToSignUpFragmentCallback()
	}


	//Validazione della password
	suspend fun validation(): Boolean {

		if (!AccountManager.isEmailValid(email)) throw Exception("'E-mail' non valida")
		if (AccountManager.isEmailUsed(email)) throw Exception("'E-mail' già in uso")
		if (password.length < MIN_LENGHT) throw Exception("'Password' deve essere di almeno "+ MIN_LENGHT + " caratteri")
		if (password.length > MAX_LENGHT) throw Exception("'Password' deve essere al più di "+ MAX_LENGHT + " caratteri")
		if (password != repeat_password) throw Exception("'Password' deve essere uguale a\n'Ripeti password'")

		for (i in pass_strong()) {
			if (!i) return false
		}
		return true
	}


	/*Regular expression per verificare che:

	1) Deve iniziare con una o più lettere, cifre o caratteri di sottolineatura \w+.
	2) Può contenere un punto o un trattino seguito da una o più lettere, cifre o caratteri di sottolineatura ([.-]?\\w+)*.
	3) Deve contenere il simbolo '@'.
	4) Dopo l'@, deve seguire una o più lettere, cifre o caratteri di sottolineatura \\w+.
	5) Può contenere un punto o un trattino seguito da una o più lettere, cifre o caratteri di sottolineatura ([.-]?\\w+)*.
	6) Alla fine, deve terminare con un punto seguito da due o tre lettere \\.\\w{2,3}.
	 */

	/*
	0-No input
	1-Weak
	2-Normal
	3-Strong
	4-VeryStrong
	*/
	fun pass_strong(): List<Boolean> {

		val caratteri = password.toCharArray()

		//For password check
		var checkChar: Boolean = false
		var checkCharUp: Boolean = false
		var checkNumber: Boolean = false
		var checkSpecial: Boolean = false

		for (i in 0..caratteri.size - 1) {
			if (!checkChar) {
				if (caratteri[i] in character) checkChar = true
			}
			if (!checkCharUp) {
				if (caratteri[i] in character_up) checkCharUp = true
			}
			if (!checkNumber) {
				if (caratteri[i] in number) checkNumber = true
			}
			if (!checkSpecial) {
				if (caratteri[i] in special) checkSpecial = true
			}
		}
		return listOf<Boolean>(checkChar, checkCharUp, checkSpecial, checkNumber)

	}


	suspend fun signUp() {
			if (validation()) {
				MainScope().launch {
					AccountManager.signup(email, password, AccountManager.user)
					goToMainActivityCallback()
				}
			}
	}


}