package it.univpm.spottedkotlin.managers

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.model.User
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


object AccountManager {
	private val auth = Firebase.auth
	/*lateinit*/ var user: User = User("Valerio", "Morelli", RemoteImages.AVATAR.url).apply {
		uid = "Lj1dlqZAREdLnzjsJ6mM2F08SnUc"
	}

	//SALVARE IN MEMORIA L'ID (MANAGER I/O con chiave valore e chiave uid)

	fun cacheLogin(): User? = null


	private suspend fun handleAuthResult(authResult: AuthResult?) {
		if (authResult != null) {
			val user: User? = DatabaseManager.get("users/${auth.currentUser?.uid}")
			if (user != null) {
				this.user = user
			} else {
				// Utente NON trovato in FirebaseRealtimeDatabase

			}
		} else {
			// Utente NON trovato in FirebaseAuth

			//TODO(user non loggato == MESSAGGIO DI ERRORE)

			/*3 casi  ERRORE SPECIFICO
			-trovato
			-trovato ma non nel database
			-non trovato*/
		}
	}

	suspend fun login(email: String, password: String) {
		val authResult = auth.signInWithEmailAndPassword(email, password).await()
		handleAuthResult(authResult)
	}

	suspend fun login(account: GoogleSignInAccount) {
		val credential = GoogleAuthProvider.getCredential(account.idToken, null)
		val authResult = auth.signInWithCredential(credential).await()
		handleAuthResult(authResult)
	}

	fun signup(email: String, password: String) {
		auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
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