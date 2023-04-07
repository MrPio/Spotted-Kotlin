package it.univpm.spottedkotlin.managers

import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.model.User

object AccountManager {
	var user: User = User("Valerio", "Morelli", RemoteImages.AVATAR.url).apply {
		uid = "Lj1dlqZAREdLnzjsJ6mM2F08SnUc"
	}

	fun cacheLogin(): User? = null
	fun login(): User? = null
	fun signup(): User? = null
}