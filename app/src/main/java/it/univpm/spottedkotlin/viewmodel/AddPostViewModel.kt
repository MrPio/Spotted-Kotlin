package it.univpm.spottedkotlin.viewmodel

import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.enums.Locations

class AddPostViewModel : ViewModel() {
	val plessi= Locations.values().map { it.title }
	val zone= Locations.values().map { it.title }
}