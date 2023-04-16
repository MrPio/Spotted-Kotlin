package it.univpm.spottedkotlin.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.LoginFragmentBinding
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.viewmodel.LoginViewModel

class LoginFragment : Fragment() {
	private lateinit var binding: LoginFragmentBinding
	private val viewModel: LoginViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = LoginFragmentBinding.inflate(inflater, container, false)
		viewModel.goToMainActivityCallback = ::goToMainActivity
		binding.viewModel=viewModel



		binding.registerText.setOnClickListener {
			binding.root.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
		}
		return binding.root


	}

	private fun goToMainActivity(){
		requireActivity().runOnUiThread { startActivity(Intent(activity, MainActivity::class.java)) }
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
