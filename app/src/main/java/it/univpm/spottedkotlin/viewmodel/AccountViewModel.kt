package it.univpm.spottedkotlin.viewmodel

import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.managers.AccountManager

class AccountViewModel : ViewModel() {

    var name: String = AccountManager.user.name +" "+ AccountManager.user.surname
    lateinit var nameInsta:String

	// TODO: Implement the ViewModel
}