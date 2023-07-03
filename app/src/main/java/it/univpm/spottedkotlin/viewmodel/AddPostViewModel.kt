package it.univpm.spottedkotlin.viewmodel

import androidx.databinding.Bindable
import it.univpm.spottedkotlin.BR
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.enums.Locations
import it.univpm.spottedkotlin.enums.Plexuses
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.extension.ObservableViewModel
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.model.Post
import java.util.Calendar

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
		get() =
			if (latitude == null)
				currentPlesso.locations[zona].imageUrl
			else
				RemoteImages.ANCONA.url

	@get:Bindable
	var plesso: Int
		get() = Plexuses.values().indexOf(currentPlesso)
		set(value) {
			currentPlesso = Plexuses.values()[value]
			currentZona = currentPlesso.locations[0]
			nuovoPost.location = currentZona
			notifyPropertyChanged(BR.zone)
			notifyPropertyChanged(BR.zona)
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
		get() = listOf(AccountManager.user.name+" "+ AccountManager.user.surname, "Anonimo")

	@get:Bindable
	var author: Int = 0

	@get:Bindable
	var errors: String = ""

	@get:Bindable
	var latitude: Double? = null

	@get:Bindable
	var longitude: Double? = null
	fun azzera() {
		author = 0
		currentPlesso = Plexuses.INGEGNERIA
		currentZona = Locations.QT_140
		currentGender = Gender.FEMALE
		nuovoPost = Post().apply {
			location = currentZona
			authorUID = AccountManager.user.uid
		}
		this.errors = ""
		notifyChange()
		loadTagsCallback()
	}

	fun pubblica(): Boolean {
		this.errors = ""

		// Check if posting in anonymously
		nuovoPost.anonymous = author == 1

		// Validate the model and print any error
		val errors = nuovoPost.validate()
		if (errors.isEmpty()) {
			if (latitude != null && longitude != null) {
				nuovoPost.location = null
				nuovoPost.latitude = latitude
				nuovoPost.longitude = longitude
			}
			nuovoPost.timestamp = Calendar.getInstance().time.time
			DataManager.save(nuovoPost, mode = DataManager.SaveMode.POST)
			azzera()
		} else
			this.errors = errors.joinToString(separator = "\n") { "• $it" }
		notifyPropertyChanged(BR.errors)
		return errors.isEmpty()
	}

	fun removeCoordinates() {
		latitude = null
		longitude = null
		notifyChange()
	}
}