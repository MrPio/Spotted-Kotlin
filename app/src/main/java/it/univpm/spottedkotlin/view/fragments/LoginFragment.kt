package it.univpm.spottedkotlin.view.fragments

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.LoginFragmentBinding
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.viewmodel.FirstActivityViewModel
import it.univpm.spottedkotlin.viewmodel.LoginViewModel

class LoginFragment : Fragment() {
	private lateinit var binding: LoginFragmentBinding
	private val viewModel: LoginViewModel by viewModels()
	private lateinit var auth: FirebaseAuth

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = LoginFragmentBinding.inflate(inflater, container, false)
		auth = Firebase.auth

		binding.loginButton.setOnClickListener {

			//Prendo i campi e-mail e password
			val email: String = binding.TextEmailAddress.text.toString()
			val pass: String = binding.TextPassword.text.toString()

			//Authentication da firebase
			auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener() { task ->
				if (task.isSuccessful) {
					// Sign in success, update UI with the signed-in user's information
					Log.d(ContentValues.TAG, "signInWithEmail:success")
					val user = auth.currentUser
					//Se ha avuto successo prendo dal database l'utente corrispondente
					if (user != null) {
						AccountManager.login(user)
						startActivity(Intent(activity, MainActivity::class.java))
					}
				} else {
					// If sign in fails, display a message to the user.
					Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
					Toast.makeText(
						context, "Authentication failed.",
						Toast.LENGTH_SHORT
					).show()
					//TODO(user non loggato == MESSAGGIO DI ERRORE)
				}
			}
		}


		binding.registerText.setOnClickListener {
			binding.root.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
		}
		return binding.root


	}

}


//TODO()Quando l'utente avvia l'applicazione si controlla se è loggato
/*public override fun onStart() {
	super.onStart()
	// Check if user is signed in (non-null) and update UI accordingly.
	val currentUser = auth.currentUser
	if(currentUser != null){
		reload()
	}
}*/
