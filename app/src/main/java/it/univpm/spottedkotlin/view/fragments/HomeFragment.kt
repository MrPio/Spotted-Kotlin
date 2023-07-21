package it.univpm.spottedkotlin.view.fragments

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.univpm.spottedkotlin.adapter.HomePostsAdapter
import it.univpm.spottedkotlin.adapter.TagsAdapter
import it.univpm.spottedkotlin.databinding.HomeFragmentBinding
import it.univpm.spottedkotlin.databinding.OrderbyPopupBinding
import it.univpm.spottedkotlin.databinding.SelectTagPopupBinding
import it.univpm.spottedkotlin.enums.TimesInterpolator
import it.univpm.spottedkotlin.extension.function.*
import it.univpm.spottedkotlin.managers.AnimationManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.model.Filter
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.view.AccountActivity
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.view.SettingsActivity
import it.univpm.spottedkotlin.viewmodel.HomeViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min

class HomeFragment : Fragment() {
	private lateinit var binding: HomeFragmentBinding
	private val viewModel: HomeViewModel by viewModels()
	private val adapter = HomePostsAdapter(mutableListOf())
	private lateinit var layoutManager: LinearLayoutManager
	private var scaffoldHeight: Int = 0
	private var topExpanded: Boolean? = false
	private var lastScrollY = 300
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {
		binding = HomeFragmentBinding.inflate(inflater, container, false)
		layoutManager = LinearLayoutManager(context)
		binding.apply {
			homeAvatar.setOnClickListener {
				activity?.goto<AccountActivity>()
			}
			homeMenu.setOnClickListener {
				activity?.goto<SettingsActivity>()
			}
			homeOrderby.setOnClickListener {
				orderBy()
			}
		}
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		binding.homeScaffold.viewTreeObserver.addOnGlobalLayoutListener(object :
			OnGlobalLayoutListener {
			override fun onGlobalLayout() {
				binding.homeScaffold.viewTreeObserver.removeOnGlobalLayoutListener(this);
				scaffoldHeight = binding.homeScaffold.height
				binding.postsRecycler.translationY = scaffoldHeight.toFloat()
			}
		})
		binding.postsRecycler.layoutManager = layoutManager
		viewModel.reloadCallback = ::reload
		binding.viewModel = viewModel
		binding.homePostsAdapter = adapter

		// Load more posts on scroll
		binding.postsRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
				super.onScrollStateChanged(recyclerView, newState)
				if (layoutManager.itemCount == layoutManager.findLastVisibleItemPosition() + 1) MainScope().launch { recyclerLoadMore() }
			}

			override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
				super.onScrolled(recyclerView, dx, dy)
				onRecyclerScroll()
			}
		})

		observe()
	}

	// Observe the subtitle MutableLiveData
	private fun observe() {
		viewModel.subtitle.observe(viewLifecycleOwner) {
			binding.homeSubtitle.text = requireContext().getString(it)
		}
	}

	// Expand UI on scroll
	private fun onRecyclerScroll() {
		val y = binding.postsRecycler.computeVerticalScrollOffset()

		if (topExpanded == false && (lastScrollY - y < -150)) {
			topExpanded = null
			requireContext().getActivity<MainActivity>()?.binding?.bottomBarContainer?.animate()
				?.translationY(400f)?.setDuration(300L)?.start()
			AnimationManager.animate(start = 20.toDp(), end = 0, update = {
				binding.homeScaffold.setPadding(0, 0, 0, it.toInt())
			})
			AnimationManager.animate(
				start = 0,
				end = scaffoldHeight - 80,
				interpolator = TimesInterpolator.LINEAR,
				update = { binding.homeScaffold.setHeight(scaffoldHeight - it.toInt()) },
				endListener = {
					lastScrollY = y - scaffoldHeight;topExpanded = true
				},
			)
			binding.postsRecycler.animate().translationY(80f).setDuration(250).start()


		} else if (topExpanded == true && (lastScrollY - y > 150 || y < 10)) {
			topExpanded = null
			requireContext().getActivity<MainActivity>()?.binding?.bottomBarContainer?.animate()
				?.translationY(0f)?.setDuration(300L)?.start()
			AnimationManager.animate(start = 0, end = 20.toDp(), update = {
				binding.homeScaffold.setPadding(0, 0, 0, it.toInt())
			})
			AnimationManager.animate(
				start = scaffoldHeight,
				end = 0,
				interpolator = TimesInterpolator.LINEAR,
				update = {
					binding.homeScaffold.setHeight(scaffoldHeight - it.toInt())
				},
				endListener = {
					lastScrollY = y + scaffoldHeight;topExpanded = false
				},
			)
			binding.postsRecycler.animate().translationY(scaffoldHeight.toFloat()).setDuration(250)
				.start()

		} else if (topExpanded == false && lastScrollY - y > 500) lastScrollY = y + 500
		else if (topExpanded == true && lastScrollY - y < -500) lastScrollY = y - 500
	}

	// Load another posts' page to the db if available
	private suspend fun recyclerLoadMore() {
		requireActivity().runOnUiThread {
			binding.homeLoadingView.loadingViewRoot.visibility = View.VISIBLE
		}

		viewModel.requestMorePosts()
		if (viewModel.posts.size != adapter.posts.size - 1) {
			adapter.posts = viewModel.posts.subList(
				0, min(viewModel.posts.size, DataManager.pageSize * ++adapter.loaded)
			).toMutableList()
			adapter.posts.add(null)
			adapter.notifyDataSetChanged()
		}
		activity?.runOnUiThread {
			binding.homeLoadingView.loadingViewRoot.visibility = View.GONE
		}
	}

	// Reset the adapter pointer, reload the posts from the db and display them from scratch
	private suspend fun reload() {
		adapter.loaded = 0
		adapter.posts = mutableListOf()
		viewModel.reloadPosts()
		DataManager.filterPosts(viewModel.filter)
		recyclerLoadMore()
	}

	// Reload on resume
	override fun onResume() {
		super.onResume()
		MainScope().launch {
			reload()
		}
	}

	// Display the OrderByPopup, change the filter and reload data
	private fun orderBy() {
		val popupBinding = OrderbyPopupBinding.inflate(layoutInflater, null, false)
		when (viewModel.filter.orderBy) {
			Filter.OrderBy.DATE -> popupBinding.orderbyDate.isChecked = true
			Filter.OrderBy.RELEVANCE -> popupBinding.orderbyRelevance.isChecked = true
			else -> {}
		}

		context?.showAlertDialog(
			title = "Scegli la modalità di ordinamento dei post",
			view = popupBinding.root,
			positiveText = "Conferma",
			positiveCallback = {
				if (popupBinding.orderbyRelevance.isChecked) viewModel.filter.orderBy =
					Filter.OrderBy.RELEVANCE
				else if (popupBinding.orderbyDate.isChecked) viewModel.filter.orderBy =
					Filter.OrderBy.DATE
				MainScope().launch {
					reload()
				}
			})
	}
}
