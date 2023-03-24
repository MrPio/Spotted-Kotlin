package it.univpm.spottedkotlin.viewmodel

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.view.View
import androidx.fragment.app.commit
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.*
import it.univpm.spottedkotlin.databinding.ActivityMainBinding
import it.univpm.spottedkotlin.databinding.BottomBarBinding
import it.univpm.spottedkotlin.extension.getActivity
import it.univpm.spottedkotlin.extension.rawX
import it.univpm.spottedkotlin.extension.rawY

class MainViewModel : ViewModel() {
	val currentFragment: MutableLiveData<Int> by lazy { MutableLiveData<Int>(0) }
	private var lastIndex = 0
	val fragments = listOf(HomeFragment(), MapFragment())
	val bottomBarFragment = BottomBarFragment()

	fun bottomBarItemClicked(index: Int) {
		currentFragment.value = index
	}

	fun currentFragmentChanged(binding: ActivityMainBinding): Unit {
		val activity = binding.root.context.getActivity<MainActivity>()!!
		val b = bottomBarFragment.binding
		val circles = listOf(
			b.homeCircle,
			b.mapCircle,
			b.addPostCircle,
			b.accountCircle,
			b.settingsCircle
		)
		val icons = listOf(
			b.homeIcon,
			b.mapIcon,
			b.addPostIcon,
			b.accountIcon,
			b.settingsIcon
		)
		val currIndex: Int = currentFragment.value ?: 0
		for ((i, pair) in circles.zip(icons).withIndex()) {
			if (i != currIndex) pair.first.visibility = View.INVISIBLE
			pair.second.setTextColor(activity.getColor(if (i == currIndex) R.color.color1 else R.color.color4))
		}
		if (currIndex != lastIndex) {
			//Circle Animation
			b.animationCircle.x = circles[lastIndex].rawX()
			b.animationCircle.y = circles[lastIndex].rawY() - 68
			b.animationCircle.visibility = View.VISIBLE
			b.animationCircle.animate().x(
				circles[currIndex].rawX()
			).setListener(object : AnimatorListenerAdapter() {
				override fun onAnimationEnd(animation: Animator) {
					circles[currIndex].visibility = View.VISIBLE
				}
			}).start()

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