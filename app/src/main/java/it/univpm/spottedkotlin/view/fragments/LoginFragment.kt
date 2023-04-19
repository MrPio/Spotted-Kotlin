package it.univpm.spottedkotlin.view.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.LoginFragmentBinding
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.viewmodel.LoginViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
	private lateinit var binding: LoginFragmentBinding
	private val viewModel: LoginViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		//ActivityResult for Google sign-in activity
		val resultLauncher =
			registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
				if (result.resultCode == Activity.RESULT_OK) {
					val task =
						GoogleSignIn.getSignedInAccountFromIntent(result.data)
					MainScope().launch {
						try {
							val account = task.getResult(ApiException::class.java)!!
							AccountManager.login(account)
							goToMainActivity()
						} catch (_: ApiException) {
						} catch (e: Exception) {
							if (e.message?.contains("user not found in database") == true) {

							} else if (e.message?.contains("user not found in auth") == true) {

							}
						}
					}
				}
			}

		binding = LoginFragmentBinding.inflate(inflater, container, false)
		viewModel.goToMainActivityCallback = ::goToMainActivity
		binding.viewModel = viewModel


		binding.registerText.setOnClickListener {
			binding.root.findNavController()
				.navigate(R.id.action_loginFragment_to_registerFragment)
		}

		binding.loginGoogleButton.setOnClickListener {
			val mGoogleSignInClient = GoogleSignIn.getClient(
				requireActivity(),
				GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
					.requestIdToken(getString(R.string.default_web_client_id))
					.requestEmail()
					.build()
			)
			resultLauncher.launch(mGoogleSignInClient.signInIntent)
		}
		return binding.root
	}

	private fun goToMainActivity() {
		requireActivity().runOnUiThread {
			startActivity(
				Intent(
					activity,
					MainActivity::class.java
				)
			)
		}
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
