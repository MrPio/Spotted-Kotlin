package it.univpm.spottedkotlin.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.univpm.spottedkotlin.adapter.SearchUserAdapter
import it.univpm.spottedkotlin.databinding.SearchUserFragmentBinding
import it.univpm.spottedkotlin.extension.function.getActivity
import it.univpm.spottedkotlin.extension.function.log
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.viewmodel.SearchUserViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min

class SearchUserFragment(val uid: String? = null) : Fragment() {

	private lateinit var binding: SearchUserFragmentBinding
	private lateinit var searchUserLayoutManager: LinearLayoutManager
	private val searchUserAdapter = SearchUserAdapter(mutableListOf())
	val viewModel: SearchUserViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		binding = SearchUserFragmentBinding.inflate(inflater, container, false)
		searchUserLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
		binding.searchUserRecycler.layoutManager = searchUserLayoutManager
		binding.searchUserRecycler.adapter = searchUserAdapter
		binding.searchUserRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
				super.onScrollStateChanged(recyclerView, newState)
				if (searchUserLayoutManager.itemCount == searchUserLayoutManager.findLastVisibleItemPosition() + 1)
					if (viewModel.loadMore())
						MainScope().launch {
							binding.searchUsersLoadingView.root.visibility = View.VISIBLE
							delay(300)
							binding.searchUsersLoadingView.root.visibility = View.GONE
						}
			}

			override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
				super.onScrolled(recyclerView, dx, dy)
				context?.getActivity<MainActivity>()?.binding?.bottomBarContainer?.translationY =
					binding.searchUserRecycler.computeVerticalScrollOffset().toFloat()
			}
		})

		binding.viewModel = viewModel
		viewModel.searchUsers()
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel.users.observe(requireActivity()) { userList ->
			val originalSize=searchUserAdapter.users.size
			userList.forEach { user ->
				if (searchUserAdapter.users.find { it.uid == user.uid } == null)
					searchUserAdapter.users.add(user)
			}
			searchUserAdapter.notifyItemRangeInserted(originalSize,searchUserAdapter.users.size-originalSize)
		}
	}
}

