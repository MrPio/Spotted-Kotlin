package it.univpm.spottedkotlin.viewmodel

import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.enums.Plexuses
import it.univpm.spottedkotlin.model.Filter

class HomeViewModel() : ViewModel() {
	lateinit var reloadCallback: () -> Unit
	private val subtitles = listOf(
		R.string.home_ingegneria_subtitle,
		R.string.home_agraria_subtitle,
		R.string.home_scienze_subtitle,
		R.string.home_economia_subtitle,
		R.string.home_medicina_subtitle,
		R.string.home_altri_subtitle,
	)
	private val radios = listOf(
		R.id.radio_ingegneria,
		R.id.radio_agraria,
		R.id.radio_scienze,
		R.id.radio_economia,
		R.id.radio_medicina,
		R.id.radio_altri,
	)
	private val plexuses = listOf(
		Plexuses.INGEGNERIA,
		Plexuses.AGRARIA,
		Plexuses.SCIENZE,
		Plexuses.ECONOMIA,
		Plexuses.MEDICINA,
		Plexuses.ALTRI,
	)
	val subtitle = MutableLiveData(subtitles[0])
	var filter=Filter(plexus = Plexuses.INGEGNERIA)
	fun onRadioCheckedChanged(group: RadioGroup, checkedId: Int) {
		val index = radios.indexOf(checkedId)
		val isChecked: Boolean =
			(group.findViewById(checkedId) as RadioButton).isChecked
		if (isChecked)
			subtitle.value = subtitles[index]
		filter=Filter(plexus = plexuses[index])
		reloadCallback()
	}
}