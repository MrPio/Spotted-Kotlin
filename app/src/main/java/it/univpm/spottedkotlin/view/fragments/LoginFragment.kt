package it.univpm.spottedkotlin.view.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
import it.univpm.spottedkotlin.view.FirstActivity
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.viewmodel.LoginViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginFragment : Fragment() {
    private lateinit var binding: LoginFragmentBinding
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        googleSignInClient = GoogleSignIn.getClient(
            requireActivity(),
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        )

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
                        } catch (e: Exception) {
                            binding.error.text = e.message
                        }
                    }
                }
            }

        binding = LoginFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        binding.registerText.setOnClickListener {
            binding.root.findNavController()
                .navigate(R.id.action_loginFragment_to_signUpGeneralFragment)
        }

        binding.loginButton.setOnClickListener {

            if (binding.TextPassword.text.toString() == "") {
                binding.TextPassword.background =
                    context?.let { ContextCompat.getDrawable(it, R.drawable.text_view_border_red) }
            } else {
                binding.TextPassword.background =
                    context?.let { ContextCompat.getDrawable(it, R.drawable.text_view_border_grey) }
            }

            if (!AccountManager.isEmailValid(viewModel.email)) {
                binding.TextEmail.background =
                    context?.let { ContextCompat.getDrawable(it, R.drawable.text_view_border_red) }
                binding.error.setTextColor(Color.RED)
                binding.error.text = "E-mail non valida"
            } else {
                binding.TextEmail.background =
                    context?.let { ContextCompat.getDrawable(it, R.drawable.text_view_border_grey) }
                binding.error.text = ""
            }

            if (binding.TextPassword.text.toString() != "" && AccountManager.isEmailValid(viewModel.email)) {

                MainScope().launch {
                    try {
                        viewModel.login()
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    } catch (e: Exception) {
                        binding.error.setTextColor(Color.RED)
                        binding.error.text = e.message
                    }

                }

            }
        }


        binding.loginGoogleButton.setOnClickListener {
            resultLauncher.launch(googleSignInClient.signInIntent)
        }
        binding.TextPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE)
                binding.loginButton.performClick()
            return@setOnEditorActionListener true
        }

        return binding.root
    }

    private fun goToMainActivity() {
        (activity as FirstActivity).goToMainActivity()
    }
}