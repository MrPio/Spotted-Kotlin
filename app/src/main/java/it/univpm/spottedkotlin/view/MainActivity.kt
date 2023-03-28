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

		//BottomBar Observer
		viewModel.currentFragment.observe(this) {
			viewModel.currentFragmentChanged(binding)
		}

//		DummyManager.generatePosts()

	}
}


