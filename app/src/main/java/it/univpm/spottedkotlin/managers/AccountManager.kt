package it.univpm.spottedkotlin.managers

import android.widget.Toast
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import it.univpm.spottedkotlin.enums.RemoteImages
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