package it.univpm.spottedkotlin

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import it.univpm.spottedkotlin.databinding.ActivityMainBinding
import it.univpm.spottedkotlin.extension.*
import it.univpm.spottedkotlin.managers.DeviceManager
import it.univpm.spottedkotlin.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
	private val viewModel: MainViewModel by viewModels()
	private var lastIndex = 0
	private val fragments = listOf(HomeFragment(), MapFragment())
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		DeviceManager.displayMetrics = this.metrics()

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
		binding.viewModel = viewModel
		binding.executePendingBindings()
		supportFragmentManager.commit {
			add(
				binding.mainFragmentContainer.id,
				fragments[0]
			) // name can be null
		}

		binding.homeBottomBar.viewModel = viewModel
		//BottomBar Observer
		viewModel.currentFragment.observe(this) {
			val b = binding.homeBottomBar
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
			val currIndex: Int = viewModel.currentFragment.value ?: 0
			for ((i, pair) in circles.zip(icons).withIndex()) {
				if (i != currIndex) pair.first.visibility = View.INVISIBLE
				pair.second.setTextColor(this.getColor(if (i == currIndex) R.color.color1 else R.color.color4))
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
				supportFragmentManager.commit {
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
		binding.homeBottomBar.executePendingBindings()
	}
}


