package it.univpm.spottedkotlin.managers

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import it.univpm.spottedkotlin.managers.LogManager.TAG
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.User
import kotlinx.coroutines.tasks.await


object AccountManager {
    private val auth = Firebase.auth
    lateinit var user: User
    private var MESSAGE_NO_CONNECTION =
        "Qualcosa è andato storto!\nControlla la tua connessione e riprova!"
    val userPosts: MutableList<Post> = mutableListOf()
    val isUserInitialized get() = ::user.isInitialized

    // Register new FirebaseAuth account and create new User object
    suspend fun signup(email: String, password: String, newUser: User) {
        try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            signUpHandleAuthResult(authResult, newUser)
        } catch (e: Exception) {
            throw Exception(MESSAGE_NO_CONNECTION)
        }

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
            DataManager.loadUserFollowingPosts(user)
            return user != DataManager.anonymous
        }
        return false
    }

    // Perform FirebaseAuth login with email and password
    suspend fun login(email: String, password: String) {
        try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            loginHandleAuthResult(authResult)
        } catch (e: Exception) {
            throw Exception("E-mail o password errate")
            }
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
            DataManager.loadUserFollowingPosts(user)
            if (user == DataManager.anonymous)
                throw Exception("Credenziali non corrette.\nControlla e riprova")
        } else
            throw Exception("user not found in auth")
    }


    // Remove cached login
    fun logout() {
        IOManager.removeKey("user_uid")
        auth.signOut()
    }

    // Send email to change the password
    fun sendChangePasswordEmail() {
        if (auth.currentUser == null)
            throw Exception("User not logged")
        if (auth.currentUser!!.email == null)
            throw Exception("No available email")

        auth.sendPasswordResetEmail(auth.currentUser!!.email!!)
    }

    suspend fun isEmailUsed(email: String): Boolean {
        try {
            val methods = auth.fetchSignInMethodsForEmail(email).await()
            if (!(methods.signInMethods.isNullOrEmpty())) return true
        } catch (e: Exception) {
            throw Exception(MESSAGE_NO_CONNECTION)
        }
        return false
    }

    /*Regular expression per verificare che:

	1) Deve iniziare con una o più lettere, cifre o caratteri di sottolineatura \w+.
	2) Può contenere un punto o un trattino seguito da una o più lettere, cifre o caratteri di sottolineatura ([.-]?\\w+)*.
	3) Deve contenere il simbolo '@'.
	4) Dopo l'@, deve seguire una o più lettere, cifre o caratteri di sottolineatura \\w+.
	5) Può contenere un punto o un trattino seguito da una o più lettere, cifre o caratteri di sottolineatura ([.-]?\\w+)*.
	6) Alla fine, deve terminare con un punto seguito da due o tre lettere \\.\\w{2,3}.
	 */
    fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$")
        return email.matches(emailRegex)
    }
}