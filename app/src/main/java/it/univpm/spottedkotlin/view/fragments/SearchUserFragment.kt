package it.univpm.spottedkotlin.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import it.univpm.spottedkotlin.adapter.SearchUserAdapter
import it.univpm.spottedkotlin.databinding.SearchUserFragmentBinding
import it.univpm.spottedkotlin.viewmodel.SearchUserViewModel

class SearchUserFragment(val uid: String? = null) : Fragment() {

	private lateinit var binding: SearchUserFragmentBinding
	val viewModel: SearchUserViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		binding = SearchUserFragmentBinding.inflate(inflater, container, false)
		binding.searchUserRecycler.layoutManager=
			LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		binding.viewModel = viewModel
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.users.observe(requireActivity()){
			binding.searchUserRecycler.adapter=SearchUserAdapter(it)
		}
	}
}

