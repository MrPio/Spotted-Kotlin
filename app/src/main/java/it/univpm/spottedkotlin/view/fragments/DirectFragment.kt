package it.univpm.spottedkotlin.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.adapter.SearchUserAdapter
import it.univpm.spottedkotlin.databinding.DirectFragmentBinding
import it.univpm.spottedkotlin.databinding.SearchUserFragmentBinding
import it.univpm.spottedkotlin.extension.function.getActivity
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.viewmodel.DirectViewModel
import it.univpm.spottedkotlin.viewmodel.SearchUserViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DirectFragment : Fragment() {

	private lateinit var binding: DirectFragmentBinding
	private lateinit var chatsLayoutManager: LinearLayoutManager
	val viewModel: DirectViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DirectFragmentBinding.inflate(inflater, container, false)
		chatsLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

		binding.viewModel = viewModel
		return binding.root
	}
}