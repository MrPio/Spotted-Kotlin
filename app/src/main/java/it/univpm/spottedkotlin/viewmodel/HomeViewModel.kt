package it.univpm.spottedkotlin.viewmodel

import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.enums.Plexuses
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.model.Filter
import it.univpm.spottedkotlin.model.Post
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
	lateinit var reloadCallback: suspend () -> Unit

	var name: String? = AccountManager.user.name
	val avatar: String = AccountManager.user.avatar
	val posts: List<Post> get() = DataManager.posts.toList().toList()

	private val subtitles = listOf(
		R.string.home_ingegneria_subtitle,
		R.string.home_agraria_subtitle,
		R.string.home_scienze_subtitle,
		R.string.home_economia_subtitle,
		R.string.home_medicina_murri_subtitle,
		R.string.home_medicina_eustacchio_subtitle,
		R.string.home_ancona_subtitle,
		R.string.home_altri_subtitle,
	)
	private val radios = listOf(
		R.id.radio_ingegneria,
		R.id.radio_agraria,
		R.id.radio_scienze,
		R.id.radio_economia,
		R.id.radio_medicina_murri,
		R.id.radio_medicina_eustacchio,
		R.id.radio_ancona,
		R.id.radio_altri,
	)
	private val plexuses = Plexuses.values().toList()
	val subtitle = MutableLiveData(subtitles[0])
	var filter = Filter(
		plexus = Plexuses.INGEGNERIA,
		exceptUsers = listOf(AccountManager.user.uid?:""),
		exceptSpotted = false
	)

	fun onRadioCheckedChanged(group: RadioGroup, checkedId: Int) {
		val index = radios.indexOf(checkedId)
		val isChecked: Boolean =
			(group.findViewById(checkedId) as RadioButton).isChecked
		if (isChecked)
			subtitle.value = subtitles[index]
		filter.apply {
			plexus = plexuses[index]
			orderBy = filter.orderBy
		}
		MainScope().launch {
			reloadCallback()
		}
	}

	suspend fun requestMorePosts() {
		DataManager.loadMore()
		DataManager.filterPosts(filter)
	}

	fun reloadPosts() = DataManager.reloadPaginatedData()
}