package it.univpm.spottedkotlin.view

import android.Manifest
import android.R
import android.app.AlertDialog
import android.bluetooth.BluetoothClass.Device
import android.content.Intent
import android.hardware.display.DisplayManager
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import it.univpm.spottedkotlin.databinding.FirstActivityBinding
import it.univpm.spottedkotlin.enums.Settings
import it.univpm.spottedkotlin.extension.function.checkAndAskPermission
import it.univpm.spottedkotlin.extension.function.log
import it.univpm.spottedkotlin.extension.function.metrics
import it.univpm.spottedkotlin.extension.function.showAlertDialog
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
		initializeManagers()
		binding = FirstActivityBinding.inflate(layoutInflater)
		setContentView(binding.root)
		binding.viewModel = viewModel
		NotificationManager.notify(this, "ciao", "descrdescrdescrdescrdescr")

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
//WorkerManager.startNotificationWorker(this)
		return
//☢️☢️☢️☢️☢️☢️☢️☢️☢️☢️☢️☢️

		binding.firstLoadingView.loadingViewRoot.visibility = View.VISIBLE
		MainScope().launch {
			if (AccountManager.cacheLogin())
				goToMainActivity()
			runOnUiThread {
				binding.firstLoadingView.loadingViewRoot.visibility = View.GONE
			}
		}
	}
}