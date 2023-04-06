package it.univpm.spottedkotlin.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.ActivityMainBinding
import it.univpm.spottedkotlin.extension.function.metrics
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.managers.DatabaseManager
import it.univpm.spottedkotlin.managers.DeviceManager
import it.univpm.spottedkotlin.managers.DummyManager
import it.univpm.spottedkotlin.model.Comment
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.Tag
import it.univpm.spottedkotlin.viewmodel.MainViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

	lateinit var binding: ActivityMainBinding
	val viewModel: MainViewModel by viewModels()
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		DeviceManager.displayMetrics = this.metrics()

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
		binding.viewModel = viewModel
		binding.executePendingBindings()
		supportFragmentManager.commit {
			add(
				binding.mainFragmentContainer.id, viewModel.fragments[0]
			)
			add(
				binding.bottomBarContainer.id, viewModel.bottomBarFragment
			)
		}
		observe()
	}

	private fun observe() {
		viewModel.currentFragment.observe(this) {
			val currIndex: Int = viewModel.currentFragment.value ?: 0

			if (currIndex != viewModel.lastIndex)
				viewModel.bottomBarFragment.changeIndex(viewModel.lastIndex, currIndex)

			//Transition with Animation framework
			supportFragmentManager.commit {
				remove(viewModel.fragments[viewModel.lastIndex])
				setCustomAnimations(
					if (currIndex > viewModel.lastIndex) R.anim.slide_in_right else R.anim.slide_in_left,
					R.anim.fade_out,
				)
				add(
					binding.mainFragmentContainer.id,
					viewModel.fragments[currIndex]
				)
			}
			viewModel.lastIndex = currIndex
		}
	}
}


