package it.univpm.spottedkotlin.view.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.SignupFragmentBinding
import it.univpm.spottedkotlin.databinding.SignupGeneralFragmentBinding
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.viewmodel.SignUpGeneralViewModel
import it.univpm.spottedkotlin.viewmodel.SignUpViewModel


class SignUpGeneralFragment : Fragment() {
    private lateinit var binding: SignupGeneralFragmentBinding
    private val viewModel: SignUpGeneralViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignupGeneralFragmentBinding.inflate(inflater, container, false)
        viewModel.goToMainActivityCallback = ::goToMainActivity
        binding.viewModel=viewModel



        binding.doLoginText.setOnClickListener {
            //binding.root.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.googleSignupButton.setOnClickListener {
          //google button
        }

        return binding.root
    }

    private fun goToMainActivity(){
        //TODO(rivedere il graph)
        //binding.root.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        requireActivity().runOnUiThread { startActivity(Intent(activity, MainActivity::class.java)) }
    }

}