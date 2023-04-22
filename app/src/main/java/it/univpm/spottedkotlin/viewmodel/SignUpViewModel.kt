package it.univpm.spottedkotlin.viewmodel

import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.managers.AccountManager
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class SignUpViewModel : ViewModel() {

    private val character : String = "abcdefghijklmnopqrstuvwxyz"
    private val character_up : String = character.uppercase()
    private val number : String = "0123456789"
    private val special : String = "!\"#\$€%&'()*+,-./:;<=>?@[\\]^_`{|}~"

    //Almost 6 character
    private val MIN_LENGHT : Int = 6

    var name: String = "Mattia"
    var surname: String = "Sbattella"
    var instaUrl: String = "instagram.com"

    var email:String ="secondario69@gmail.com"
    var password : String ="aA0%aa"
    var repeat_password : String ="aA0%aa"
    lateinit var goToMainActivityCallback : ()->Unit
    lateinit var goToSignUpFragmentCallback : ()->Unit


    fun setInfo(){
        AccountManager.setInfo(name, surname)
        goToSignUpFragmentCallback()
    }

    //Validazione della password
    fun validation(): Boolean {

        if(password.length < MIN_LENGHT) return false
        if(password != repeat_password) return false

        for(i in pass_strong()){
            if(!i) return false
        }
        return true
    }

    /*
    0-No input
    1-Weak
    2-Normal
    3-Strong
    4-VeryStrong
    */
    fun pass_strong(): List<Boolean> {

        val caratteri = password.toCharArray()

        //For password check
        var checkChar : Boolean = false
        var checkCharUp : Boolean = false
        var checkNumber : Boolean = false
        var checkSpecial : Boolean = false

        for (i in 0..caratteri.size-1){
            if (!checkChar){if(caratteri[i] in character) checkChar=true}
            if (!checkCharUp){if(caratteri[i] in character_up) checkCharUp=true}
            if (!checkNumber){if(caratteri[i] in number) checkNumber=true}
            if (!checkSpecial){if(caratteri[i] in special) checkSpecial=true}
        }
        return listOf<Boolean>(checkChar,checkCharUp,checkSpecial,checkNumber)

    }


    fun signUp(){
        try {
            if (validation()) {
                MainScope().launch {
                    println("\n\n\n\n\n\n\n"+ name+"se è vuoto =Null")
                    AccountManager.signup(email, password)
                    goToMainActivityCallback()
                }
            }
            else {}

        }
        catch (/*mia eccezione*/ _:Exception){}

    }




}