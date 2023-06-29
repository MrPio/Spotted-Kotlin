package it.univpm.spottedkotlin.managers

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.extension.function.log
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.User
import kotlinx.coroutines.tasks.await


object AccountManager {
    private val auth = Firebase.auth

    lateinit var user: User
    private var name: String = ""
    private var surname: String = ""
    private var instaUrl: String? = null
    private var gender: Gender? = null

    val userPost: MutableList<Post> = mutableListOf()

    suspend fun cacheLogin(context: Context): Boolean {
        val uid = SharedPreferencesManager.read(context)
        if (uid != "none") {
            DatabaseManager.get<User>("users/${uid}")?.let {
                user = it;
                user.uid = uid
                for (Uid in user.posts){
                    DatabaseManager.get<Post>("posts/"+Uid)?.let { userPost.add(it) }
                }
            }

            return ::user.isInitialized
        } else return false
    }

    fun logout(context: Context) {
        SharedPreferencesManager.remove(context)
        auth.signOut()
    }

    private suspend fun loginHandleAuthResult(authResult: AuthResult?) {
        if (authResult != null) {
            val uid = authResult.user?.uid
            DatabaseManager.get<User>("users/${uid}")?.let {
                user = it;
                user.uid = uid
                for (Uid in user.posts){
                    DatabaseManager.get<Post>("posts/"+Uid)?.let { userPost.add(it) }
                }
            }
            if (!::user.isInitialized)
                throw Exception("user not found in database")
        } else {
            throw Exception("user not found in auth")
        }
    }

    suspend fun login(email: String, password: String) {
        val authResult = auth.signInWithEmailAndPassword(email, password).await()
        loginHandleAuthResult(authResult)
    }

    suspend fun login(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        val authResult = auth.signInWithCredential(credential).await()
        loginHandleAuthResult(authResult)
    }


    suspend fun signup(email: String, password: String) {
        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
        var newUser = User(name, surname, instagramNickname = instaUrl, gender = gender)
        signUpHandleAuthResult(authResult, newUser)
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


    fun setInfo(name: String, surname: String, instaUrl: String?, gender: Gender?) {
        this.name = name
        this.surname = surname
        this.instaUrl = instaUrl
        this.gender = gender
    }
}