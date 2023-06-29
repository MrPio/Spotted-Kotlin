package it.univpm.spottedkotlin.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import it.univpm.spottedkotlin.databinding.FirstActivityBinding
import it.univpm.spottedkotlin.extension.function.log
import it.univpm.spottedkotlin.extension.function.metrics
import it.univpm.spottedkotlin.managers.*
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.viewmodel.FirstActivityViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FirstActivity : AppCompatActivity() {

	lateinit var binding: FirstActivityBinding
	val viewModel: FirstActivityViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		DeviceManager.displayMetrics = this.metrics()
		MainScope().launch {
			DataManager.fetchData();
		}

		binding = FirstActivityBinding.inflate(layoutInflater)

		//TODO() transizione a MainActivity se utente già loggato
		setContentView(binding.root)
		binding.viewModel = viewModel
	}

	fun goToMainActivity() {
		runOnUiThread {
			startActivity(Intent(this, MainActivity::class.java))
			finish()
		}
	}
	override fun onStart() {
		super.onStart()

		// ======= DEBUG ZONE ========
		//☢️☢️☢️☢️☢️☢️☢️☢️☢️☢️☢️☢️

//		SeederManager.seed(applicationContext)
//		return

		//☢️☢️☢️☢️☢️☢️☢️☢️☢️☢️☢️☢️

		binding.firstLoadingView.loadingViewRoot.visibility = View.VISIBLE
		MainScope().launch {
			if (AccountManager.cacheLogin(applicationContext)) goToMainActivity()
			runOnUiThread {
				binding.firstLoadingView.loadingViewRoot.visibility = View.GONE
			}
		}
	}
}