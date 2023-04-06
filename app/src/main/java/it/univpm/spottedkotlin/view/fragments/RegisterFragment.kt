package it.univpm.spottedkotlin.view.fragments

import android.content.ContentValues.TAG
import android.inputmethodservice.KeyboardView
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnKeyListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.LoginFragmentBinding
import it.univpm.spottedkotlin.databinding.RegisterFragmentBinding


class RegisterFragment : Fragment() {
    private lateinit var binding: RegisterFragmentBinding

    private lateinit var auth: FirebaseAuth

    private val character : String = "abcdefghijklmnopqrstuvwxyz"
    private val character_up : String = character.uppercase()
    private val number : String = "0123456789"
    private val special : String = "!\"#\$%&'()*+,-./:;<=>?@[\\]^_`{|}~"

    //Almost 6 character
    private val MIN_LENGHT : Int = 6
    private var strong : Int = 0


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
        /*binding.RegisterPassword.setOnClickListener(){

            val x = OnKeyListener()
            Thread(Runnable {
                while(true) {
                    //validation(binding.RegisterPassword.text.toString(),"")
                    binding.button222.setBackgroundColor(0x000000)
                }
            }).start()
        }*/

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

        for (i in 0..caratteri.size){
            if (!checkChar){if(caratteri[i] in character) checkChar=true; strong+=1}
            if (!checkCharUp){if(caratteri[i] in character_up) checkCharUp=true; strong+=1}
            if (!checkNumber){if(caratteri[i] in number) checkNumber=true; strong+=1}
            if (!checkSpecial){if(caratteri[i] in special) checkSpecial=true; strong+=1}
        }
        /*
        0-No input
        1-Weak
        2-Normal
        3-Strong
        4-VeryStrong
        */

        if(pass.length < MIN_LENGHT) return false
        if(pass != rep_pass) return false
        return checkChar && checkCharUp && checkNumber && checkSpecial
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