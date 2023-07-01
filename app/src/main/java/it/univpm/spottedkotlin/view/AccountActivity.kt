package it.univpm.spottedkotlin.view

import android.os.Bundle
import android.transition.ChangeImageTransform
import android.transition.Explode
import android.transition.Slide
import android.view.Window
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.ActivityAccountBinding
import it.univpm.spottedkotlin.databinding.ActivityMainBinding
import it.univpm.spottedkotlin.extension.function.metrics
import it.univpm.spottedkotlin.managers.*
import it.univpm.spottedkotlin.model.Comment
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.Tag
import it.univpm.spottedkotlin.view.fragments.AccountFragment
import it.univpm.spottedkotlin.viewmodel.AccountViewModel
import it.univpm.spottedkotlin.viewmodel.MainViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AccountActivity : AppCompatActivity() {
	lateinit var binding: ActivityAccountBinding
	val viewModel: AccountViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityAccountBinding.inflate(layoutInflater)
		setContentView(binding.root)
		binding.viewModel = viewModel
		binding.executePendingBindings()

		supportFragmentManager.commit {
			add(
				binding.accountContainer.id, AccountFragment()
			)
		}
	}
}


