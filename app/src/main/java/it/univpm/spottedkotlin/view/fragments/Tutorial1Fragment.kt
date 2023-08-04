package it.univpm.spottedkotlin.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.FirstFragmentBinding
import it.univpm.spottedkotlin.databinding.Tutorial1FragmentBinding
import it.univpm.spottedkotlin.viewmodel.FirstActivityViewModel


class Tutorial1Fragment : Fragment() {
	private lateinit var binding: Tutorial1FragmentBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = Tutorial1FragmentBinding.inflate(inflater, container, false)
		return binding.root
	}
}