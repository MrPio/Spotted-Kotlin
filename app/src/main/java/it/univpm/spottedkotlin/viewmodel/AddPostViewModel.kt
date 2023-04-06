package it.univpm.spottedkotlin.viewmodel

import android.app.AlertDialog
import android.app.Application
import androidx.databinding.Bindable
import it.univpm.spottedkotlin.BR
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.enums.Locations
import it.univpm.spottedkotlin.enums.Plexuses
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.extension.ObservableAndroidViewModel
import it.univpm.spottedkotlin.extension.ObservableViewModel
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.managers.DatabaseManager
import it.univpm.spottedkotlin.model.Post
import kotlin.reflect.KFunction

class AddPostViewModel() : ObservableViewModel() {
	lateinit var loadTagsCallback: () -> Unit
	val plessi = Plexuses.values().map { it.title }
	var genders = Gender.values().map { it.title }

	var currentPlesso = Plexuses.INGEGNERIA
	var currentZona = Locations.QT_140
	var nuovoPost: Post = Post().apply { location = currentZona }

	@get:Bindable
	val image: String
		get() = currentPlesso.locations[zona].imageUrl

	@get:Bindable
	var plesso: Int
		get() = Plexuses.values().indexOf(currentPlesso)
		set(value) {
			currentPlesso = Plexuses.values()[value]
			currentZona = currentPlesso.locations[0]
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

	fun azzera() {
		currentPlesso = Plexuses.INGEGNERIA
		currentZona = Locations.QT_140
		currentGender = Gender.FEMALE
		nuovoPost = Post().apply { location = Locations.QT_140 }
		notifyChange()
		loadTagsCallback()
	}

	fun pubblica() {
		DataManager.posts?.add(nuovoPost)
		DatabaseManager.post("posts", nuovoPost)
		azzera()
	}
}