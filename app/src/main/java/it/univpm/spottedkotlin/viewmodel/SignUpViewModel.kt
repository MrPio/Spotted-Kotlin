package it.univpm.spottedkotlin.viewmodel

import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.model.User
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class SignUpViewModel : ViewModel() {

	private val character: String = "abcdefghijklmnopqrstuvwxyz"
	private val character_up: String = character.uppercase()
	private val number: String = "0123456789"
	private val special: String = "!\"#\$€%&'()*+,-./:;<=>?@[\\]^_`{|}~"

	//At least 6 character
	private val MIN_LENGHT: Int = 6

	var name: String = ""
	var surname: String = ""
	var instaUrl: String = ""
	var gender: Gender? = null
	var email: String = ""
	var password: String = ""
	var repeat_password: String = ""
	lateinit var goToMainActivityCallback: () -> Unit
	lateinit var goToSignUpFragmentCallback: () -> Unit
	lateinit var user: User


	fun setInfo() {
		println("\n\n\n" + gender.toString() + "null? \n\n\n")
		user = User(name = name, surname = surname, instagramNickname = instaUrl, gender = gender)
		goToSignUpFragmentCallback()
	}


	//Validazione della password
	fun validation(): Boolean {

		if (password.length < MIN_LENGHT) return false
		if (password != repeat_password) return false

		for (i in pass_strong()) {
			if (!i) return false
		}
		return true
	}

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


	fun signUp() {
		try {
			if (validation()) {
				MainScope().launch {
					AccountManager.signup(email, password,user)
					goToMainActivityCallback()
				}
			} else {
			}

		} catch (/*mia eccezione*/ _: Exception) {
		}

	}


}