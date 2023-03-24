package it.univpm.spottedkotlin.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import it.univpm.spottedkotlin.databinding.AccountFragmentBinding
import it.univpm.spottedkotlin.viewmodel.AccountViewModel

class AccountFragment : Fragment() {
	private lateinit var binding: AccountFragmentBinding
	private val viewModel: AccountViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = AccountFragmentBinding.inflate(inflater, container, false)
		return binding.root
	}
}