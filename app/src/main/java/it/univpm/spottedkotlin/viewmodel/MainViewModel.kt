package it.univpm.spottedkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.view.fragments.*

class MainViewModel : ViewModel() {

	val currentFragment: MutableLiveData<Int> = MutableLiveData<Int>(0)
	var lastIndex = 0
	val bottomBarFragment = BottomBarFragment()
	val fragments =
		listOf(
			HomeFragment(),
			MapFragment(),
			AddPostFragment(),
			AccountFragment(),
			SettingsFragment()
		)

	fun bottomBarItemClicked(index: Int) {
		currentFragment.value = index
	}
}