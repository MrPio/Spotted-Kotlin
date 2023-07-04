package it.univpm.spottedkotlin.view

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import it.univpm.spottedkotlin.databinding.FirstActivityBinding
import it.univpm.spottedkotlin.extension.function.checkAndAskPermission
import it.univpm.spottedkotlin.extension.function.metrics
import it.univpm.spottedkotlin.managers.*
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.viewmodel.FirstActivityViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class FirstActivity : AppCompatActivity() {

	lateinit var binding: FirstActivityBinding
	val viewModel: FirstActivityViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		initializeManagers()
		binding = FirstActivityBinding.inflate(layoutInflater)
		setContentView(binding.root)
		binding.viewModel = viewModel
	}

	private fun initializeManagers() {
		DeviceManager.displayMetrics = this.metrics()
		IOManager.initialize(baseContext)
		MainScope().launch {
			DataManager.fetchData(applicationContext);
		}
		DeviceManager.loadTheme()
		DeviceManager.loadUiDensity(this)
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
			this.checkAndAskPermission(Manifest.permission.POST_NOTIFICATIONS)
	}

	fun goToMainActivity(activity: Class<*> = MainActivity::class.java) {
		runOnUiThread {
			val intent = Intent(this, activity).apply {
				if (activity == ViewPostActivity::class.java)
					this.putExtra("postUID", intent.getStringExtra("postUID"))
						.putExtra("comments", intent.getBooleanExtra("comments", false))
			}
			startActivity(intent)
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
			if (AccountManager.cacheLogin()) {
				if (intent.hasExtra("goto"))
					goToMainActivity(intent.getSerializableExtra("goto") as Class<*>)
				else
					goToMainActivity()

			}
			runOnUiThread {
				binding.firstLoadingView.loadingViewRoot.visibility = View.GONE
			}
		}
	}
}