package it.univpm.spottedkotlin.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.ActivityMainBinding
import it.univpm.spottedkotlin.extension.function.metrics
import it.univpm.spottedkotlin.managers.DatabaseManager
import it.univpm.spottedkotlin.managers.DeviceManager
import it.univpm.spottedkotlin.model.Comment
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.Tag
import it.univpm.spottedkotlin.viewmodel.MainViewModel
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
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

//		DatabaseManager.post(
//			"posts", Post(
//				"uid",
//				listOf("a", "b"),
//				listOf(Comment("c", "Ciao!")),
//				tags = listOf(Tag("Ciccio",baseContext.getString(R.string.CircleDouble)))
//			)
//		)

//		DatabaseManager.getList<Post>("posts") {
//			var c = it
//		}

//		DatabaseManager.get<String>("posts/-NRTiYUnNSY8KzY5J0B5") {
//			var c = it
//		}
	}
}


