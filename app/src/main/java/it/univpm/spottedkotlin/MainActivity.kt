package it.univpm.spottedkotlin

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import it.univpm.spottedkotlin.databinding.ActivityMainBinding
import it.univpm.spottedkotlin.viewmodel.HomeViewModel
import it.univpm.spottedkotlin.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
	private val viewModel: MainViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)
		binding.viewModel = viewModel
		binding.executePendingBindings()

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
			val currIndex = viewModel.currentFragment.value
			for ((i, pair) in circles.zip(icons).withIndex()) {
				pair.first.visibility = if (i == currIndex) View.VISIBLE else View.INVISIBLE
				pair.second.setTextColor(this.getColor(if (i == currIndex) R.color.color1 else R.color.color4))
			}
			if (currIndex == 1)
				findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_Home_to_Map)
		}
		binding.homeBottomBar.executePendingBindings()
	}
}