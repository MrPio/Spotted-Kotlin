package it.univpm.spottedkotlin.viewmodel

import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.R

class HomeViewModel : ViewModel() {

	private val subtitles = listOf(
		R.string.home_persone_subtitle,
		R.string.home_feste_subtitle,
		R.string.home_sport_subtitle,
	)
	private val radios = listOf(
		R.id.radio_persone,
		R.id.radio_feste,
		R.id.radio_sport,
	)
	val subtitle = MutableLiveData(subtitles[0])

	fun onRadioCheckedChanged(group: RadioGroup, checkedId: Int): Unit {
		val index = radios.indexOf(checkedId)
		val isChecked: Boolean =
			(group.findViewById(checkedId) as RadioButton).isChecked
		if (isChecked)
			subtitle.value = subtitles[index]
	}
}