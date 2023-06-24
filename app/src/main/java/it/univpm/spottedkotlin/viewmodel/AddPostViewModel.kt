package it.univpm.spottedkotlin.viewmodel

import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import it.univpm.spottedkotlin.BR
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.enums.Locations
import it.univpm.spottedkotlin.enums.Plexuses
import it.univpm.spottedkotlin.extension.ObservableViewModel
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.managers.DatabaseManager
import it.univpm.spottedkotlin.model.Post

class AddPostViewModel : ObservableViewModel() {
	lateinit var loadTagsCallback: () -> Unit
	val plessi = Plexuses.values().map { it.title }
	var genders = Gender.values().map { it.title }

	var currentPlesso = Plexuses.INGEGNERIA
	var currentZona = Locations.QT_140
	var nuovoPost: Post = Post().apply {
		location = currentZona
		authorUID = AccountManager.user.uid
	}

	@get:Bindable
	val image: String
		get() = currentPlesso.locations[zona].imageUrl

	@get:Bindable
	var plesso: Int
		get() = Plexuses.values().indexOf(currentPlesso)
		set(value) {
			currentPlesso = Plexuses.values()[value]
			currentZona = currentPlesso.locations[0]
			nuovoPost.location = currentZona
			notifyPropertyChanged(BR.zone)
			notifyPropertyChanged(BR.image)
			notifyPropertyChanged(BR.plesso)
		}


	@get:Bindable
	val zone: List<String?>
		get() = Plexuses.values()[plesso].locations.map { it.title }

	@get:Bindable
	var zona: Int
		get() = currentPlesso.locations.indexOf(currentZona)
		set(value) {
			currentZona = currentPlesso.locations[value]
			nuovoPost.location = currentZona
			notifyPropertyChanged(BR.zona)
			notifyPropertyChanged(BR.image)
		}

	@get:Bindable
	var currentGender = Gender.FEMALE

	@get:Bindable
	var gender: Int
		get() = currentGender.ordinal
		set(value) {
			currentGender = Gender.values()[value]
			nuovoPost.gender = currentGender
			notifyPropertyChanged(BR.gender)
			notifyPropertyChanged(BR.currentGender)
		}

	@get:Bindable
	val autori: List<String?>
		get() = listOf(AccountManager.user.instagramNickname, "Anonimo")

	@get:Bindable
	var autore: Int = 0

	fun azzera() {
		currentPlesso = Plexuses.INGEGNERIA
		currentZona = Locations.QT_140
		currentGender = Gender.FEMALE
		nuovoPost = Post().apply { location = Locations.QT_140 }
		notifyChange()
		loadTagsCallback()
	}

	fun pubblica() {
		if (autore == 1)
			nuovoPost.authorUID = null
		nuovoPost.uid = DatabaseManager.post("posts", nuovoPost)
		DataManager.posts?.add(nuovoPost)
		azzera()
	}
}