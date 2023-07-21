package it.univpm.spottedkotlin.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import it.univpm.spottedkotlin.BR
import it.univpm.spottedkotlin.extension.ObservableViewModel
import it.univpm.spottedkotlin.extension.function.log
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.model.User

class SearchUserViewModel : ObservableViewModel() {

	private val loadingStep = 14
	private var loaded = 0
		set(value) {
			field = value
			users.value = _users.takeLast(value)
		}
	private var _users = listOf<User>()

	@get:Bindable
	val users = MutableLiveData(listOf<User>())

	@get:Bindable
	var searchToken: String = ""
		set(value) {
			field = value
			searchUsers()
			notifyPropertyChanged(BR.users)
			notifyPropertyChanged(BR.searchToken)
		}

	fun searchUsers() {
		_users = DataManager.cachedUsers.filter {
			(it.name + it.surname + it.name + it.instagramNickname).lowercase()
				.contains(searchToken.lowercase())
		}
		loaded = loadingStep
	}

	fun loadMore(): Boolean {
		loaded += loadingStep
		return loaded - loadingStep < _users.size
	}

	fun reset() {
		if (searchToken != "")
			searchToken = ""
	}
}

