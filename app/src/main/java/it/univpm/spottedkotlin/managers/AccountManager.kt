package it.univpm.spottedkotlin.managers

import android.content.ContentValues.TAG
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.model.User
import kotlinx.coroutines.tasks.await


object AccountManager {
	private lateinit var auth : FirebaseAuth
	lateinit var oneTapClient: SignInClient
	lateinit var signInRequest: BeginSignInRequest

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

					//Creo un nuovo utente  TODO(mettere più info e nome vero)
					val newUser = User("Dario","Second")
					newUser.uid = auth.currentUser?.uid

					//Lo metto nel Database
					DatabaseManager.put("users/${newUser.uid}", newUser)

					//Imposto lo user corrente come nuovo User
					user=newUser

					//Invio una mail per la verifica
					auth.currentUser!!.sendEmailVerification()
						.addOnCompleteListener { task ->
							if (task.isSuccessful) {
								Log.d(TAG, "Email sent.")
							}
						}
				} else {



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