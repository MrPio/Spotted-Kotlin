package it.univpm.spottedkotlin.viewmodel

import it.univpm.spottedkotlin.extension.ObservableViewModel
import it.univpm.spottedkotlin.model.User

class DirectViewModel : ObservableViewModel() {
	val chats= listOf<User>()
}

