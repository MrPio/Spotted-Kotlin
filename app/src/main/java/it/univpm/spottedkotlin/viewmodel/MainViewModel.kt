package it.univpm.spottedkotlin.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.univpm.spottedkotlin.extension.function.log
import it.univpm.spottedkotlin.managers.WorkerManager
import it.univpm.spottedkotlin.view.fragments.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

	val currentFragment: MutableLiveData<Int> = MutableLiveData<Int>(0)
	var lastIndex = 0
	val bottomBarFragment = BottomBarFragment()
	val fragments =
		listOf(
			HomeFragment(),
			MapFragment(),
			AddPostFragment(),
			AccountFragment(),
			SearchUserFragment(),
		)

	fun initialize(context: Context) {
		"Launching NotificationWorker".log()
		viewModelScope.launch {
			WorkerManager.startPeriodicWorker<WorkerManager.NotificationWorker>(
				context,
				"NotificationWorker"
			)
		}
	}

	fun bottomBarItemClicked(index: Int) {
		currentFragment.value = index
	}
}