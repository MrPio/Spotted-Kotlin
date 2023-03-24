package it.univpm.spottedkotlin.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import it.univpm.spottedkotlin.databinding.AddPostFragmentBinding
import it.univpm.spottedkotlin.viewmodel.AddPostViewModel

class AddPostFragment : Fragment() {
	private lateinit var binding: AddPostFragmentBinding
	private val viewModel: AddPostViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = AddPostFragmentBinding.inflate(inflater, container, false)
		return binding.root
	}
}