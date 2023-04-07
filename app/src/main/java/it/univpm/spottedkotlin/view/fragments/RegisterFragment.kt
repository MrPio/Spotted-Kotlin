package it.univpm.spottedkotlin.view.fragments

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.RegisterFragmentBinding
import java.io.Console


class RegisterFragment : Fragment() {
    private lateinit var binding: RegisterFragmentBinding

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
        binding = RegisterFragmentBinding.inflate(inflater, container, false)
        auth = Firebase.auth

        binding.registerButton.setOnClickListener(){
            val email : String = binding.RegisterEmailAddress.text.toString()
            val pass : String = binding.RegisterPassword.text.toString()
            val rep_pass  : String = binding.ControlPassword.text.toString()


            if(validation(pass,rep_pass)){
                auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success")
                            val user = auth.currentUser
                            //TODO(User subito loggato oppure deve effettuare il login?)
                            Toast.makeText(context, "",
                                Toast.LENGTH_SHORT).show()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(context, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }


        //TODO(Barra per la password che cambia colore)
        binding.RegisterPassword.addTextChangedListener(object : TextWatcher{

            override fun beforeTextChanged(charSequence : CharSequence, i : Int, i1 : Int, i2 : Int) {}

            override fun onTextChanged(charSequence : CharSequence, i : Int, i1 : Int, i2 : Int) {
                // Recupera il testo dalla casella di testo
                val input : String = binding.RegisterPassword.getText().toString()

                when (pass_strong(input)) {
                    0 -> binding.button222.setBackgroundColor(Color.TRANSPARENT)
                    1 -> binding.button222.setBackgroundColor(Color.RED)
                    2 -> binding.button222.setBackgroundColor(Color.YELLOW)
                    3 -> binding.button222.setBackgroundColor(Color.YELLOW)
                    4 -> binding.button222.setBackgroundColor(Color.GREEN)
                    else -> { // Note the block
                        binding.button222.setBackgroundColor(Color.TRANSPARENT)
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

        if(pass.length < MIN_LENGHT) return false
        if(pass != rep_pass) return false
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
        Toast.makeText(context, strong.toString(),
            Toast.LENGTH_SHORT).show()
        return strong
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            //TODO(Reload)
        }
    }


}