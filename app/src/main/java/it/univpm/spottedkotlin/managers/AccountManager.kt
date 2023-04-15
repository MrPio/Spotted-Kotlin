package it.univpm.spottedkotlin.managers

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.model.User
import it.univpm.spottedkotlin.view.MainActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


object AccountManager {
	private lateinit var auth : FirebaseAuth
	/*lateinit*/ var user: User= User("Valerio", "Morelli", RemoteImages.AVATAR.url).apply { uid = "Lj1dlqZAREdLnzjsJ6mM2F08SnUc" }

	//SALVARE IN MEMORIA L'ID (MANAGER I/O con chiave valore e chiave uid)

	fun cacheLogin(): User? = null


	suspend fun login(email:String, password: String) {
		auth = Firebase.auth
		val authresult  = auth.signInWithEmailAndPassword(email, password).await()
		if (authresult != null) {
			// Sign in success, update UI with the signed-in user's information
			user = DatabaseManager.get("users/${auth.currentUser?.uid}")!!
		}

		else {
			// If sign in fails, display a message to the user.
			//TODO(user non loggato == MESSAGGIO DI ERRORE)

			/*3 casi  ERRORE SPECIFICO
			-trovato
			-trovato ma non nel database
			-non trovato*/
			}
	}



	fun signup(email:String, password: String){
		auth = Firebase.auth
		auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener() { task ->
				if (task.isSuccessful) {
					// Sign in success, update UI with the signed-in user's information
					Log.d(TAG, "createUserWithEmail:success")
					MainScope().launch {
						user = DatabaseManager.get("users/${auth.currentUser?.uid}")!!
					}
				} else {
					// If sign in fails, display a message to the user.
					Log.w(TAG, "createUserWithEmail:failure", task.exception)
					//TODO(user non registrato == MESSAGGIO DI ERRORE)
				}
			}
	}
}