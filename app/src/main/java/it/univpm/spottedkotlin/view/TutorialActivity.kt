package it.univpm.spottedkotlin.view

import android.os.Bundle
import android.view.MotionEvent
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.ActivityTutorialBinding
import it.univpm.spottedkotlin.extension.function.goto
import it.univpm.spottedkotlin.managers.IOManager

class TutorialActivity : AppCompatActivity() {

	private lateinit var binding: ActivityTutorialBinding
	private var index = 0;

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityTutorialBinding.inflate(layoutInflater)
		setContentView(binding.root)
		binding.tutorialContainer.setOnClickListener{
			if (index>=2){
				goto<FirstActivity>()
				IOManager.writeKey("first_access",false)
				return@setOnClickListener
			}
			binding.tutorialContainer.findNavController().navigate(
				if (index == 0) R.id.tutorial_fragment_1_to_tutorial_fragment_2 else R.id.tutorial_fragment_2_to_tutorial_fragment_3
			)
			index++
		}
	}
}