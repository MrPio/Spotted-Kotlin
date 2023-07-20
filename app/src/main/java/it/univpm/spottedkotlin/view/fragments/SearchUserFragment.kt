package it.univpm.spottedkotlin.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import it.univpm.spottedkotlin.databinding.SearchUserFragmentBinding
import it.univpm.spottedkotlin.viewmodel.SearchUserViewModel

class SearchUserFragment(val uid: String? = null) : Fragment() {

	private lateinit var binding: SearchUserFragmentBinding
	val viewModel: SearchUserViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {

		binding = SearchUserFragmentBinding.inflate(inflater, container, false)
		binding.viewModel = viewModel
		return binding.root
	}
}

