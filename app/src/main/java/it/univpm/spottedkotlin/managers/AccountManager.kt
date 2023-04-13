package it.univpm.spottedkotlin.managers

import com.google.firebase.auth.FirebaseUser
import it.univpm.spottedkotlin.model.User


object AccountManager {
	lateinit var user: User
	//var user: User = User("Valerio", "Morelli", RemoteImages.AVATAR.url).apply { uid = "Lj1dlqZAREdLnzjsJ6mM2F08SnUc" }

	fun cacheLogin(): User? = null


	fun login(FirebaseUser:FirebaseUser) {

		DatabaseManager.getChild("users").child(FirebaseUser.uid).get().addOnSuccessListener {
			user = it.getValue(User::class.java)!!

		}.addOnFailureListener {
			println("ERRORE")
		}
	}

	fun signup(): User? = null
}