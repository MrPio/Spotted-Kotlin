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
import it.univpm.spottedkotlin.viewmodel.FirstActivityViewModel


class FirstFragment : Fragment() {
	private lateinit var binding: FirstFragmentBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = FirstFragmentBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.startButton.setOnClickListener {
			binding.root.findNavController().navigate(R.id.action_firstFragment_to_loginFragment)
		}
	}
}