package it.univpm.spottedkotlin.viewmodel

import androidx.fragment.app.commit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.*
import it.univpm.spottedkotlin.databinding.ActivityMainBinding
import it.univpm.spottedkotlin.extension.function.getActivity
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.view.fragments.*

class MainViewModel : ViewModel() {
	val currentFragment: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }
	private var lastIndex = 0
	val fragments =
		listOf(
			HomeFragment(),
			MapFragment(),
			AddPostFragment(),
			AccountFragment(),
			SettingsFragment()
		)
	val bottomBarFragment = BottomBarFragment()

	fun bottomBarItemClicked(index: Int) {
		currentFragment.value = index
	}

	fun currentFragmentChanged(binding: ActivityMainBinding) {
		val activity = binding.root.context.getActivity<MainActivity>()!!
		val currIndex: Int = currentFragment.value ?: 0

		//Animating white circle and transition to the new selected fragment
		if (currIndex != lastIndex) {
			//Circle Animation
			bottomBarFragment.changeIndex(lastIndex, currIndex)

			//Transition with Animation framework
			activity.supportFragmentManager.commit {
				remove(fragments[lastIndex])
				setCustomAnimations(
					if (currIndex > lastIndex)
						R.anim.slide_in_right else R.anim.slide_in_left,
					R.anim.fade_out,
				)
				add(
					binding.mainFragmentContainer.id,
					fragments[currIndex]
				)
			}
		}

		lastIndex = currIndex
	}
}