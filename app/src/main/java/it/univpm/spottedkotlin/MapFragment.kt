package it.univpm.spottedkotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import it.univpm.spottedkotlin.databinding.MapFragmentBinding
import it.univpm.spottedkotlin.viewmodel.MapViewModel

class MapFragment : Fragment() {
	private lateinit var binding: MapFragmentBinding
	private val viewModel: MapViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = MapFragmentBinding.inflate(inflater, container, false)
		return binding.root
	}
}