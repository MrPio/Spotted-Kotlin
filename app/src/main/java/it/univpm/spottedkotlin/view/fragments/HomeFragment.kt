package it.univpm.spottedkotlin.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.univpm.spottedkotlin.adapter.HomePostsAdapter
import it.univpm.spottedkotlin.databinding.HomeFragmentBinding
import it.univpm.spottedkotlin.enums.TimesInterpolator
import it.univpm.spottedkotlin.extension.function.getActivity
import it.univpm.spottedkotlin.extension.function.log
import it.univpm.spottedkotlin.extension.function.setHeight
import it.univpm.spottedkotlin.extension.function.toDp
import it.univpm.spottedkotlin.managers.AnimationManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.viewmodel.HomeViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min

class HomeFragment : Fragment() {
	private lateinit var binding: HomeFragmentBinding
	private val viewModel: HomeViewModel by viewModels()
	private var posts: List<Post> = DataManager.posts?.toList() ?: listOf()
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
		binding.postsRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
				super.onScrollStateChanged(recyclerView, newState)
				if (layoutManager.itemCount == layoutManager.findLastVisibleItemPosition() + 1)
					recyclerLoadMore()
			}

			override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
				super.onScrolled(recyclerView, dx, dy)
				onRecyclerScroll()
			}
		})

		binding.homeLoadingView.loadingViewRoot.visibility = View.VISIBLE
		MainScope().launch {
			DataManager.fetchData(requireContext())
			activity?.runOnUiThread {
				reload()
				binding.homeLoadingView.loadingViewRoot.visibility = View.GONE
			}
		}
		observe()
	}

	private fun observe() {
		viewModel.subtitle.observe(viewLifecycleOwner) {
			binding.homeSubtitle.text = requireContext().getString(it)
		}
	}

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

	private fun recyclerLoadMore() {
		if (posts.size == adapter.posts.size-1) return
		adapter.posts = posts.subList(
			0, min(posts.size, adapter.LOADING_STEP * ++adapter.loaded)
		).toMutableList()
		adapter.posts.add(null)
		adapter.notifyDataSetChanged()

		//loadingView
		binding.homeLoadingView.loadingViewRoot.visibility = View.VISIBLE
		MainScope().launch {
			delay(400)
			activity?.runOnUiThread {
				binding.homeLoadingView.loadingViewRoot.visibility = View.GONE
			}
		}
	}

	private fun reload() {
		adapter.loaded = 0
		DataManager.sort()
		posts = DataManager.filteredPosts(viewModel.filter)
		adapter.posts= mutableListOf()
		recyclerLoadMore()
	}

	override fun onResume() {
		super.onResume()
		reload()
	}
}
