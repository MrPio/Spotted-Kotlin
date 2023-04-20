package it.univpm.spottedkotlin.managers

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.model.User
import kotlinx.coroutines.tasks.await


object AccountManager {
	private val auth=Firebase.auth
	lateinit var oneTapClient: SignInClient
	lateinit var signInRequest: BeginSignInRequest

	var user: User = User("Valerio", "Morelli", RemoteImages.AVATAR.url).apply {
		uid = "Lj1dlqZAREdLnzjsJ6mM2F08SnUc"
	}

	//SALVARE IN MEMORIA L'ID (MANAGER I/O con chiave valore e chiave uid)

	fun cacheLogin(): User? = null

	private suspend fun handleAuthResult(authResult: AuthResult?) {
		if (authResult != null) {
			println(authResult.user?.uid)
			val user: User? = DatabaseManager.get("users/${authResult.user?.uid}")
			if (user != null) {
				this.user = user
			} else {
				// Utente NON trovato in FirebaseRealtimeDatabase
				throw Exception("user not found in database")
			}
		} else {
			throw Exception("user not found in auth")
			// Utente NON trovato in FirebaseAuth
		}
	}

	suspend fun login(email: String, password: String) {
		val authResult = auth.signInWithEmailAndPassword(email,password).await()
		handleAuthResult(authResult)
	}
	suspend fun login(account: GoogleSignInAccount) {
		val credential = GoogleAuthProvider.getCredential(account.idToken, null)
		val authResult = auth.signInWithCredential(credential).await()
		handleAuthResult(authResult)
	}


	fun signup(email: String, password: String) {
		auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener() { task ->
			if (task.isSuccessful) {
				// Sign in success, update UI with the signed-in user's information

				//Creo un nuovo utente  TODO(mettere più info e nome vero)
				val newUser = User("Dario", "Second")
				newUser.uid = auth.currentUser?.uid

				//Lo metto nel Database
				DatabaseManager.put("users/${newUser.uid}", newUser)

				//Imposto lo user corrente come nuovo User
				user = newUser

				//Invio una mail per la verifica
				auth.currentUser!!.sendEmailVerification()
					.addOnCompleteListener { task ->
						if (task.isSuccessful) {
							Log.d(TAG, "Email sent.")
						}
					}
			} else {
				// Utente già registrato
				throw Exception("The email address is already in use by another account.")
			}
		}
	}


	suspend fun google_login() {
		/*var RC_SIGN_IN = 9001
		val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken("947301479273-poo7lh3cn28pk173nogoohqv36i9i99t.apps.googleusercontent.com")
			.requestEmail()
			.build()
		var mGoogleSignInClient : GoogleSignInClient = GoogleSignIn.getClient(activity, gso)
		val signInIntent = mGoogleSignInClient.signInIntent
		activity.startActivityForResult(signInIntent, 1000)*/

		/*oneTapClient = Identity.getSignInClient(this)
		signInRequest = BeginSignInRequest.builder()
			.setGoogleIdTokenRequestOptions(
				BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
					.setSupported(true)
					// Your server's client ID, not your Android client ID.
					.setServerClientId("947301479273-poo7lh3cn28pk173nogoohqv36i9i99t.apps.googleusercontent.com")
					// Only show accounts previously used to sign in.
					.setFilterByAuthorizedAccounts(true)
					.build())
			.build()


	}*/
	}
}