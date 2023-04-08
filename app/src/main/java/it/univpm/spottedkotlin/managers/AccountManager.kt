package it.univpm.spottedkotlin.managers

import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.model.User

object AccountManager {
	private lateinit var database: DatabaseReference
	lateinit var user: User
	//var user: User = User("Valerio", "Morelli", RemoteImages.AVATAR.url).apply { uid = "Lj1dlqZAREdLnzjsJ6mM2F08SnUc" }

	fun cacheLogin(): User? = null


	//TODO(funziona, trova l'utente ====> PARSING AD OGGETTO)
	fun login(FirebaseUser:FirebaseUser) {
		database = Firebase.database.reference
		database.child("users").child(FirebaseUser.uid).get().addOnSuccessListener {
			print(it.value.toString())
			//user = USER POST PARSING
		}.addOnFailureListener{
				println("ERRORE")
		}
	}

	fun signup(): User? = null
}