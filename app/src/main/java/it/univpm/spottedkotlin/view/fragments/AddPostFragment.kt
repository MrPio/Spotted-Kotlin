package it.univpm.spottedkotlin.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.ScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import it.univpm.spottedkotlin.databinding.AddPostFragmentBinding
import it.univpm.spottedkotlin.extension.function.fromDp
import it.univpm.spottedkotlin.extension.function.getActivity
import it.univpm.spottedkotlin.extension.function.setHeight
import it.univpm.spottedkotlin.extension.function.toDp
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.viewmodel.AddPostViewModel
import kotlin.math.max
import kotlin.math.min

class AddPostFragment : Fragment() {
	private lateinit var binding: AddPostFragmentBinding
	private val viewModel: AddPostViewModel by viewModels()
	private var footerHeight = 0


	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = AddPostFragmentBinding.inflate(inflater, container, false)
		binding.viewModel=viewModel
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.addPostImage.viewTreeObserver.addOnGlobalLayoutListener(object :
			ViewTreeObserver.OnGlobalLayoutListener {
			override fun onGlobalLayout() {
				binding.addPostImage.viewTreeObserver.removeOnGlobalLayoutListener(this);
				footerHeight = binding.addPostFooter.height
			}
		})

		binding.addPostScrollview.setOnScrollChangeListener { _, _, scrollY, _, _ ->
			context?.getActivity<MainActivity>()?.binding?.bottomBarContainer?.translationY =
				scrollY.toFloat()
			binding.addPostFooter.setHeight(
				max(
					96.fromDp().toInt(),
					footerHeight - scrollY
				)
			)
		}
	}
}