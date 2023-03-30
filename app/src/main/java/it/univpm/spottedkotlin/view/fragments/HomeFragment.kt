package it.univpm.spottedkotlin.view.fragments

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.core.view.doOnLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.univpm.spottedkotlin.adapter.HomePostsAdapter
import it.univpm.spottedkotlin.databinding.HomeFragmentBinding
import it.univpm.spottedkotlin.enums.TimesInterpolator
import it.univpm.spottedkotlin.extension.function.*
import it.univpm.spottedkotlin.managers.AnimationManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.viewmodel.HomeViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.min

class HomeFragment : Fragment() {
	private lateinit var binding: HomeFragmentBinding
	private val viewModel: HomeViewModel by viewModels()
	private val adapter = HomePostsAdapter(listOf())
	private lateinit var layoutManager: LinearLayoutManager
	private var scaffoldHeight: Int = 0
	private var topExpanded: Boolean? = false
	private var lastScrollY = 300
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = HomeFragmentBinding.inflate(inflater, container, false)
		layoutManager = LinearLayoutManager(context)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.postsRecycler.layoutManager = layoutManager
		binding.homeScaffold.viewTreeObserver.addOnGlobalLayoutListener(object :
			OnGlobalLayoutListener {
			override fun onGlobalLayout() {
				binding.homeScaffold.viewTreeObserver.removeOnGlobalLayoutListener(this);
				scaffoldHeight = binding.homeScaffold.height
			}
		})
		binding.viewModel = viewModel

		binding.homePostsAdapter = adapter
		fetchPosts()

		binding.executePendingBindings()
		observe()
	}

	private fun observe() {
		viewModel.subtitle.observe(viewLifecycleOwner) {
			binding.homeSubtitle.text = requireContext().getString(it)
		}
	}

	private fun fetchPosts() {
		MainScope().launch {
			requireContext().runUI {
				binding.homeLoadingView.loadingViewRoot.visibility = View.VISIBLE
			}
			DataManager.fetchData()
			context?.runUI {
				recyclerLoadMore()
				binding.homeLoadingView.loadingViewRoot.visibility = View.GONE

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
			}
		}
	}

	private fun onRecyclerScroll() {
		val y = binding.postsRecycler.computeVerticalScrollOffset()

		if (topExpanded == false && (lastScrollY - y < -150)) {
			topExpanded = null
			requireContext().getActivity<MainActivity>()?.binding?.bottomBarContainer?.animate()
				?.translationY(400f)?.setDuration(300L)?.start()
			AnimationManager.animate(
				start = 20.toDp(),
				end = 0,
				update = {
					binding.homeScaffold.setPadding(
						0,
						0,
						0,
						(it.animatedValue as Float).toInt(),
					)
				}
			)
			AnimationManager.animate(
				start = 0,
				end = scaffoldHeight - 80,
				interpolator = TimesInterpolator.LINEAR,
				update = { binding.homeScaffold.setHeight(scaffoldHeight - (it.animatedValue as Float).toInt()) },
				endListener = {
					lastScrollY = y - scaffoldHeight;topExpanded = true
				},
			)


		} else if (topExpanded == true && (lastScrollY - y > 150 || y < 10)) {
			topExpanded = null
			requireContext().getActivity<MainActivity>()?.binding?.bottomBarContainer?.animate()
				?.translationY(0f)?.setDuration(300L)?.start()
			AnimationManager.animate(
				start = 0,
				end = 20.toDp(),
				update = {
					binding.homeScaffold.setPadding(
						0,
						0,
						0,
						(it.animatedValue as Float).toInt()
					)
				}
			)
			AnimationManager.animate(
				start = scaffoldHeight,
				end = 0,
				interpolator = TimesInterpolator.LINEAR,
				update = {
					binding.homeScaffold.setHeight(scaffoldHeight - (it.animatedValue as Float).toInt())
				},
				endListener = {
					lastScrollY = y + scaffoldHeight;topExpanded = false
				},
			)
		} else if (topExpanded == false && lastScrollY - y > 500)
			lastScrollY = y + 500
		else if (topExpanded == true && lastScrollY - y < -500)
			lastScrollY = y - 500
	}

	private fun recyclerLoadMore() {
		if (DataManager.posts?.size == adapter.posts.size)
			return
		adapter.posts =
			DataManager.posts?.subList(
				0,
				min(DataManager.posts?.size ?: 0, adapter.LOADING_STEP * ++adapter.loaded)
			)?.toList()
				?: listOf()
		adapter.notifyItemRangeChanged(0, adapter.posts.size)

		//loadingView
		binding.homeLoadingView.loadingViewRoot.visibility = View.VISIBLE
		MainScope().launch {
			delay(1600)

			context?.runUI {
				binding.homeLoadingView.loadingViewRoot.visibility = View.GONE
			}
		}
	}
}
