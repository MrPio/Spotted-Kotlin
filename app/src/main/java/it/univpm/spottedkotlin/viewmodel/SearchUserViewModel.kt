package it.univpm.spottedkotlin.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import it.univpm.spottedkotlin.BR
import it.univpm.spottedkotlin.extension.ObservableViewModel
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.model.User

class SearchUserViewModel : ObservableViewModel() {
	@get:Bindable
	val users = MutableLiveData(listOf<User>())

	@get:Bindable
	var searchToken: String = ""
		set(value) {
			field = value
			if (value.isNotEmpty())
				users.value = DataManager.cachedUsers.filter {
					(it.name + it.surname + it.name + it.instagramNickname).lowercase()
						.contains(value.lowercase())
				}
			else
				users.value= listOf()
			notifyPropertyChanged(BR.users)
		}
}

