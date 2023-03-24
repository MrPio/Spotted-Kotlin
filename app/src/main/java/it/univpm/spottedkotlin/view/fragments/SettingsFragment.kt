package it.univpm.spottedkotlin.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import it.univpm.spottedkotlin.databinding.SettingsFragmentBinding
import it.univpm.spottedkotlin.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {
	private lateinit var binding: SettingsFragmentBinding
	private val viewModel: SettingsViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = SettingsFragmentBinding.inflate(inflater, container, false)
		return binding.root
	}
}