package it.univpm.spottedkotlin.managers

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.User
import kotlinx.coroutines.tasks.await


object AccountManager {
	private val auth = Firebase.auth
	lateinit var user: User
	val userPosts: MutableList<Post> = mutableListOf()
	val isUserInitialized get() = ::user.isInitialized

	// Register new FirebaseAuth account and create new User object
	suspend fun signup(email: String, password: String, newUser: User) {
		val authResult = auth.createUserWithEmailAndPassword(email, password).await()
			?: throw Exception("The email address is already in use by another account.")
		signUpHandleAuthResult(authResult, newUser)
	}

	// Store a new User objet to FirebaseRD
	private fun signUpHandleAuthResult(authResult: AuthResult, newUser: User) {
		newUser.uid = authResult.user?.uid
		DatabaseManager.put("users/${newUser.uid}", newUser)
		userPosts.clear()
		user = newUser
	}


	// Perform an automated login fetching the user's uid from the local storage
	suspend fun cacheLogin(): Boolean {
		IOManager.readKey("user_uid")?.let { uid ->
			user = DataManager.loadUser(uid as String)
			DataManager.loadUserPosts(user)
			return user != DataManager.anonymous
		}
		return false
	}

	// Perform FirebaseAuth login with email and password
	suspend fun login(email: String, password: String) {
		val authResult = auth.signInWithEmailAndPassword(email, password).await()
			loginHandleAuthResult(authResult)
	}

	// Perform FirebaseAuth login with Google provider
	suspend fun login(account: GoogleSignInAccount) {
		val credential = GoogleAuthProvider.getCredential(account.idToken, null)
		val authResult = auth.signInWithCredential(credential).await()
		loginHandleAuthResult(authResult)
	}

	// Find the user object from FirebaseRD using FirebaseAuth response
	private suspend fun loginHandleAuthResult(authResult: AuthResult?) {
		if (authResult != null) {
			val uid = authResult.user?.uid
			user = DataManager.loadUser(uid)
			DataManager.loadUserPosts(user)
			if (user == DataManager.anonymous)
				throw Exception("user not found in database")
		} else
			throw Exception("user not found in auth")
	}


	// Remove cached login
	fun logout() {
		IOManager.removeKey("user_uid")
		auth.signOut()
	}
}