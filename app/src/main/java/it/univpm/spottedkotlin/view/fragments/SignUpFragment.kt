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
import androidx.navigation.fragment.navArgs
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.SignupFragmentBinding
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.viewmodel.SignUpViewModel


class SignUpFragment : Fragment() {
    private lateinit var binding: SignupFragmentBinding
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignupFragmentBinding.inflate(inflater, container, false)
        viewModel.goToMainActivityCallback = ::goToMainActivity
        binding.viewModel=viewModel


        //Controllo della robustezza della password ogni volta che cambia un carattere
        binding.RegisterPassword.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}

            override fun afterTextChanged(editable: Editable) {
                strong()
            }
        })

        binding.doLoginText.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
        }

        binding.googleSignupButton.setOnClickListener {
          //google button
        }

        return binding.root
    }

    private fun goToMainActivity(){
        requireActivity().runOnUiThread { startActivity(Intent(activity, MainActivity::class.java)) }
    }




    /*
     0-No input
     1-Weak
     2-Normal
     3-Strong
     4-VeryStrong
     */
    fun strong(){
        val strong = viewModel.pass_strong()
        var contStrong:Int=0

        val checkChar = strong[0]
        val checkCharUp = strong[1]
        val checkSpecial = strong[2]
        val checkNumber = strong[3]

        if(checkChar){
            contStrong++
            binding.requirement1Text.setTextColor(Color.GREEN) }
        else{
            binding.requirement1Text.setTextColor(Color.GRAY) }
        if(checkCharUp){
            contStrong++
            binding.requirement2Text.setTextColor(Color.GREEN) }
        else{
            binding.requirement2Text.setTextColor(Color.GRAY) }
        if(checkSpecial){
            contStrong++
            binding.requirement3Text.setTextColor(Color.GREEN) }
        else{
            binding.requirement3Text.setTextColor(Color.GRAY) }
        if(checkNumber){
            contStrong++
            binding.requirement4Text.setTextColor(Color.GREEN) }
        else{
            binding.requirement4Text.setTextColor(Color.GRAY) }

        when (contStrong) {
            0 ->{
                binding.RegisterPassword.background=
                    context?.let { ContextCompat.getDrawable(it, R.drawable.text_view_border_grey) }
                binding.strongText.text= ""
            }

            1 ->{
                binding.RegisterPassword.background=
                    context?.let { ContextCompat.getDrawable(it, R.drawable.text_view_border_red) }
                binding.strongText.text= "Weak"
            }

            2 -> {
                binding.RegisterPassword.background=
                    context?.let { ContextCompat.getDrawable(it, R.drawable.text_view_border_orange) }
                binding.strongText.text= "Normal"}

            3 ->{
                binding.RegisterPassword.background=
                    context?.let { ContextCompat.getDrawable(it, R.drawable.text_view_border_yellow) }
                binding.strongText.text= "Normal"
            }

            4 ->{
                binding.RegisterPassword.background=
                    context?.let { ContextCompat.getDrawable(it, R.drawable.text_view_border_green) }
                binding.strongText.text= "Strong"
            }

            else -> { // Note the block
                binding.RegisterPassword.background=
                    context?.let { ContextCompat.getDrawable(it, R.drawable.text_view_border_grey) }
                binding.strongText.text= ""

            }
        }
    }




}