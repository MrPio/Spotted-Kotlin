package it.univpm.spottedkotlin.viewmodel

import androidx.lifecycle.ViewModel

class SignUpGeneralViewModel : ViewModel() {


    var name:String =""
    var surname : String =""
    lateinit var goToMainActivityCallback : ()->Unit

}