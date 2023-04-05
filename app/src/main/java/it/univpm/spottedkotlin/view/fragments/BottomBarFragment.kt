package it.univpm.spottedkotlin.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.BottomBarBinding
import it.univpm.spottedkotlin.enums.TimesInterpolator
import it.univpm.spottedkotlin.extension.function.*
import it.univpm.spottedkotlin.managers.AnimationManager
import it.univpm.spottedkotlin.view.MainActivity
import kotlin.math.abs

class BottomBarFragment : Fragment() {
	lateinit var binding: BottomBarBinding
	private lateinit var circles: List<View>
	private lateinit var icons: List<TextView>

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = BottomBarBinding.inflate(inflater, container, false)

		//Storing references of views
		circles = listOf(
			binding.homeCircle,
			binding.mapCircle,
			binding.addPostCircle,
			binding.accountCircle,
			binding.settingsCircle
		)
		icons = listOf(
			binding.homeIcon,
			binding.mapIcon,
			binding.addPostIcon,
			binding.accountIcon,
			binding.settingsIcon
		)

		//The ViewModel is shared with MainActivity
		binding.viewModel = requireContext().getActivity<MainActivity>()?.viewModel

		return binding.root
	}

	fun changeIndex(from: Int, to: Int) {
		val activity = binding.root.context.getActivity<MainActivity>()!!
		val circle = binding.animationCircle
		activity.binding.bottomBarContainer.animate().translationY(0f).start()

		//Setting bottomBar white circles and icons text colors
		for ((i, pair) in circles.zip(icons).withIndex()) {
			if (i != to)
				pair.first.visibility = View.INVISIBLE
			pair.second.setTextColor(
				activity.getColor(if (i == to) R.color.color1 else R.color.color4)
			)
		}

		//Circle Animation
		circle.x = circles[from].rawX()
		circle.y = circles[from].rawY() - 68
		circle.visibility = View.VISIBLE

		circle.animate()
			.setDuration(160L + 50 * abs(to - from))
			.setInterpolator((if (to > from) TimesInterpolator.LINEAR else TimesInterpolator.ALMOST_LINEAR).interpolator)
			.x(circles[to].rawX() + 20)
			.onEnd {
				circles[to].visibility = View.VISIBLE
				circle.visibility = View.INVISIBLE
			}
			.start()

		//Width Animation
		AnimationManager.animate(
			start = circle.width,
			end = circle.width * (1 + .5 * abs(to - from)),
			interpolator = TimesInterpolator.BELL_SIN,
			duration = 140L + 50 * abs(to - from),
			update = { circle.setWidth((it.animatedValue as Number).toInt()) }
		)
	}
}