package it.univpm.spottedkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class FirstActivityViewModel : ViewModel() {
    val currentFragment: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }
}