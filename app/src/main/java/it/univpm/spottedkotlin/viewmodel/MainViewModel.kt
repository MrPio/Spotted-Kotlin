package it.univpm.spottedkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel:ViewModel() {
	val currentFragment: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }
	fun bottomBarItemClicked(index: Int) {
		currentFragment.value = index
	}
}