package it.univpm.spottedkotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import it.univpm.spottedkotlin.adapter.HomePostsAdapter
import it.univpm.spottedkotlin.databinding.HomeFragmentBinding
import it.univpm.spottedkotlin.enums.Colors
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.Tag
import it.univpm.spottedkotlin.viewmodel.HomeViewModel
import it.univpm.spottedkotlin.extension.loadStr

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
					percentage = 11,
					locationImage = "https://firebasestorage.googleapis.com/v0/b/spotted-f3589.appspot.com/o/src%2Funi_140.jpg?alt=media&token=53f4caee-dca6-47eb-a534-dc28dc354cb4",
					tags = listOf(
						Tag("Alto", context?.loadStr(R.string.Home) ?: ""),
						Tag("Alto", context?.loadStr(R.string.Home) ?: ""),
						Tag("Alto", context?.loadStr(R.string.Home) ?: ""),
						Tag("Alto", context?.loadStr(R.string.Home) ?: ""),
					)
				)
			)
		)
		binding.executePendingBindings()
	}
}
