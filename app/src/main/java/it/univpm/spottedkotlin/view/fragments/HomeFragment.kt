package it.univpm.spottedkotlin.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.adapter.HomePostsAdapter
import it.univpm.spottedkotlin.databinding.HomeFragmentBinding
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.Tag
import it.univpm.spottedkotlin.viewmodel.HomeViewModel

class HomeFragment : Fragment() {
	private lateinit var binding: HomeFragmentBinding
	private val viewModel: HomeViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = HomeFragmentBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val postsRecycleView: RecyclerView = view.findViewById(R.id.posts_recycler)
		postsRecycleView.layoutManager = LinearLayoutManager(context)
		binding.viewModel = viewModel
		binding.homePostsAdapter = HomePostsAdapter(
			listOf(
				Post(
					locationImage = RemoteImages.QT_140.url,
					tags = listOf(
						Tag("Alto", context?.getString(R.string.Home) ?: ""),
						Tag("Alto", context?.getString(R.string.Home) ?: ""),
						Tag("Alto", context?.getString(R.string.Home) ?: ""),
						Tag("Alto", context?.getString(R.string.Home) ?: ""),
					)
				)
			)
		)
		binding.executePendingBindings()
		observe()
	}

	private fun observe(){
		viewModel.subtitle.observe(viewLifecycleOwner) {
			binding.homeSubtitle.text=requireContext().getString(it)
		}
	}
}
