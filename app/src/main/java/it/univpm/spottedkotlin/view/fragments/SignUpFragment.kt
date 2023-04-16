package it.univpm.spottedkotlin.view.fragments

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.SignupFragmentBinding
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.view.MainActivity


class SignUpFragment : Fragment() {
    private lateinit var binding: SignupFragmentBinding

    private lateinit var auth: FirebaseAuth

    private val character : String = "abcdefghijklmnopqrstuvwxyz"
    private val character_up : String = character.uppercase()
    private val number : String = "0123456789"
    private val special : String = "!\"#\$€%&'()*+,-./:;<=>?@[\\]^_`{|}~"

    //Almost 6 character
    private val MIN_LENGHT : Int = 6



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = SignupFragmentBinding.inflate(inflater, container, false)

        auth = Firebase.auth

        binding.registerButton.setOnClickListener(){
            val email : String = binding.RegisterEmailAddress.text.toString()
            val pass : String = binding.RegisterPassword.text.toString()
            val rep_pass  : String = binding.ControlPassword.text.toString()


            if(validation(pass,rep_pass)){

                AccountManager.user == null
                AccountManager.signup(email,pass)

                if (AccountManager.user != null) {
                    startActivity(Intent(activity, MainActivity::class.java))
                }
                else { }//TODO(messaggio di errore)
            }
        }


        //TODO(Barra per la password che cambia colore)
        binding.RegisterPassword.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(charSequence : CharSequence, i : Int, i1 : Int, i2 : Int) {}

            override fun onTextChanged(charSequence : CharSequence, i : Int, i1 : Int, i2 : Int) {

                val input : String = binding.RegisterPassword.getText().toString()

                when (pass_strong(input)) {
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

            override fun afterTextChanged(editable : Editable) {}
            })



        binding.doLoginText.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        return binding.root
    }



    //Validazione della password
    //TODO(i check per lo strong vanno rivisti)
    fun validation(pass: String, rep_pass : String): Boolean {

        if(pass.length < MIN_LENGHT) return false
        if(pass != rep_pass) return false

        val caratteri = pass.toCharArray()

        //For password check
        var checkChar : Boolean = false
        var checkCharUp : Boolean = false
        var checkNumber : Boolean = false
        var checkSpecial : Boolean = false

        for (i in 0..(caratteri.size-1)){
            if (!checkChar){if(caratteri[i] in character) checkChar=true}
            if (!checkCharUp){if(caratteri[i] in character_up) checkCharUp=true}
            if (!checkNumber){if(caratteri[i] in number) checkNumber=true}
            if (!checkSpecial){if(caratteri[i] in special) checkSpecial=true}
        }
        return checkChar && checkCharUp && checkNumber && checkSpecial
    }

    /*
     0-No input
     1-Weak
     2-Normal
     3-Strong
     4-VeryStrong
     */
    fun pass_strong(pass: String): Int {
        val caratteri = pass.toCharArray()
        var strong : Int = 0

        //For password check
        var checkChar : Boolean = false
        var checkCharUp : Boolean = false
        var checkNumber : Boolean = false
        var checkSpecial : Boolean = false

        for (i in 0..caratteri.size-1){
            if (!checkChar){if(caratteri[i] in character) {checkChar=true; strong++}}
            if (!checkCharUp){if(caratteri[i] in character_up){checkCharUp=true; strong++}}
            if (!checkNumber){if(caratteri[i] in number) {checkNumber=true; strong++}}
            if (!checkSpecial){if(caratteri[i] in special) {checkSpecial=true; strong++}}
        }

        if(checkChar){
            binding.requirement1Text.setTextColor(Color.GREEN)
        }
        else{
            binding.requirement1Text.setTextColor(Color.GRAY)
        }
        if(checkCharUp){
            binding.requirement2Text.setTextColor(Color.GREEN)
        }
        else{
            binding.requirement2Text.setTextColor(Color.GRAY)
        }
        if(checkSpecial){
            binding.requirement3Text.setTextColor(Color.GREEN)
        }
        else{
            binding.requirement3Text.setTextColor(Color.GRAY)
        }
        if(checkNumber){
            binding.requirement4Text.setTextColor(Color.GREEN)
        }
        else{
            binding.requirement4Text.setTextColor(Color.GRAY)
        }
        return strong
    }


}