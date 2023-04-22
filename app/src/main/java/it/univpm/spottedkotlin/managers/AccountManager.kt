package it.univpm.spottedkotlin.managers

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.model.User
import kotlinx.coroutines.tasks.await


object AccountManager {
	private val auth=Firebase.auth

//	var user: User = User("Valerio", "Morelli", RemoteImages.AVATAR.url).apply {
//		uid = "Lj1dlqZAREdLnzjsJ6mM2F08SnUc"}

	lateinit var user: User

	private var name:String=""
	private var surname:String=""


	//SALVARE IN MEMORIA L'ID (MANAGER I/O con chiave valore e chiave uid)

	fun cacheLogin(): User? = null

	private suspend fun loginHandleAuthResult(authResult: AuthResult?) {
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
		loginHandleAuthResult(authResult)
	}
	suspend fun login(account: GoogleSignInAccount) {
		val credential = GoogleAuthProvider.getCredential(account.idToken, null)
		val authResult = auth.signInWithCredential(credential).await()
		loginHandleAuthResult(authResult)
	}


	suspend fun signup(email: String, password: String, instaUrl: String? = null, gender: Gender? = null) {
		val authResult = auth.createUserWithEmailAndPassword(email, password).await()
		var newUser = User(name, surname, instagramNickname = instaUrl, gender = gender)
		signUpHandleAuthResult(authResult,newUser)
	}


	private suspend fun signUpHandleAuthResult(authResult: AuthResult?, newUser: User) {
		if (authResult != null) {
			newUser.uid = authResult.user?.uid
			DatabaseManager.put("users/${newUser.uid}", newUser)
			user = newUser

		} else {
			throw Exception("The email address is already in use by another account.")
		}
	}


	fun setInfo(name: String, surname: String){
		this.name=name
		this.surname=surname
	}
}