package it.univpm.spottedkotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import it.univpm.spottedkotlin.databinding.BottomBarBinding
import it.univpm.spottedkotlin.databinding.HomeFragmentBinding
import it.univpm.spottedkotlin.extension.getActivity
import it.univpm.spottedkotlin.viewmodel.HomeViewModel

class BottomBarFragment : Fragment() {
	lateinit var binding: BottomBarBinding

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = BottomBarBinding.inflate(inflater, container, false)
		binding.viewModel = requireContext().getActivity<MainActivity>()?.viewModel
		return binding.root
	}
}