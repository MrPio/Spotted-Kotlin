package it.univpm.spottedkotlin.viewmodel

import androidx.fragment.app.commit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.*
import it.univpm.spottedkotlin.databinding.LoginActivityBinding
import it.univpm.spottedkotlin.extension.function.getActivity
import it.univpm.spottedkotlin.view.FirstActivity
import it.univpm.spottedkotlin.view.fragments.*


class LoginViewModel : ViewModel() {
    val currentFragment: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }
}