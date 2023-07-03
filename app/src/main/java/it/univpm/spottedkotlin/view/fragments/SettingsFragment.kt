package it.univpm.spottedkotlin.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import it.univpm.spottedkotlin.databinding.SettingItemBinding
import it.univpm.spottedkotlin.databinding.SettingMenuBinding
import it.univpm.spottedkotlin.databinding.SettingsFragmentBinding
import it.univpm.spottedkotlin.enums.TimesInterpolator
import it.univpm.spottedkotlin.extension.function.fromDp
import it.univpm.spottedkotlin.extension.function.getActivity
import it.univpm.spottedkotlin.extension.function.setHeight
import it.univpm.spottedkotlin.extension.function.showAlertDialog
import it.univpm.spottedkotlin.managers.AnimationManager
import it.univpm.spottedkotlin.view.FirstActivity
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.viewmodel.SettingItemViewModel
import it.univpm.spottedkotlin.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {
	private lateinit var binding: SettingsFragmentBinding
	private val viewModel: SettingsViewModel by viewModels()
	private val settingsBindings: MutableList<SettingMenuBinding> = mutableListOf()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		binding = SettingsFragmentBinding.inflate(inflater, container, false)
		binding.viewModel = viewModel
		viewModel.gotoFirstActivityCallback = ::gotoFirstActivity

		// Hide the BottomBar with scroll
//		binding.settingsScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
//			context?.getActivity<MainActivity>()?.binding?.bottomBarContainer?.translationY =
//				scrollY.toFloat()
//		}

		binding.settingsInfo.setOnClickListener {
			context?.showAlertDialog(
				title = "Info sull'applicazione",
				message = "Spotted! - Applicazione realizzata per il progetto d'esame del corso di Programmazione Mobile del professore Emanuele Storti all'università Politecnica delle Marche (a.a. 2022/2023).\n\nAutori:\n\t• Valerio Morelli\n\t• Mattia Sbatella",
				positiveText = "Chiudi",
				positiveCallback = {},
			)
		}
		loadSettings()
		return binding.root
	}

	private fun loadSettings() {
		binding.settingsContent.removeAllViews()
		for (settingMenu in viewModel.settingMenus) {

			// Set onClick listeners
			when (settingMenu.title) {
				"Logout" -> settingMenu.onClick = {
					context?.showAlertDialog(title = "Logout dell'account",
						message = "Sicuro di voler effettuare il logout?",
						positiveCallback = viewModel::logout,
						negativeCallback = {})
				}
			}

			val settingMenuBinding =
				SettingMenuBinding.inflate(layoutInflater, binding.settingsContent, false)
			settingMenuBinding.model = settingMenu
			settingsBindings.add(settingMenuBinding)

			if (settingMenu.items != null) {

				for (settingItem in settingMenu.items) {
					val settingItemBinding = SettingItemBinding.inflate(
						layoutInflater,
						settingMenuBinding.settingMenuContent,
						false
					)
					settingItemBinding.viewModel = SettingItemViewModel(settingItem)
					settingMenuBinding.settingMenuContent.addView(settingItemBinding.root)
				}
				settingMenu.onClick = {

					// Hide all the containers
//					for (settingBinding in settingsBindings)
//						settingBinding.settingMenuContentContainer.visibility=View.GONE

					// Toggle current container visibility
					val view = settingMenuBinding.settingMenuContentContainer
					view.visibility = if (view.visibility == View.VISIBLE)
						View.GONE
					else
						View.VISIBLE
				}
			} else
				settingMenuBinding.settingMenuContentContainer.visibility = View.GONE
			binding.settingsContent.addView(settingMenuBinding.root)
		}
	}

	private fun gotoFirstActivity() {
		val intent = Intent(requireContext(), FirstActivity::class.java)
		startActivity(intent)
		requireActivity().finish()
	}
}