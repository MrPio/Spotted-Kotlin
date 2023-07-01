package it.univpm.spottedkotlin.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.GridLayout
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.adapter.TagsAdapter
import it.univpm.spottedkotlin.databinding.AddPostFragmentBinding
import it.univpm.spottedkotlin.databinding.SelectTagPopupBinding
import it.univpm.spottedkotlin.databinding.TagItemAddBinding
import it.univpm.spottedkotlin.databinding.TagItemBinding
import it.univpm.spottedkotlin.extension.function.*
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.model.Tag
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.viewmodel.AddPostViewModel
import it.univpm.spottedkotlin.viewmodel.TagItemAddViewModel
import it.univpm.spottedkotlin.viewmodel.TagItemViewModel
import kotlin.math.max


class AddPostFragment : Fragment() {
	private lateinit var binding: AddPostFragmentBinding
	var latitude: Double? = null
	var longitude: Double? = null
	val viewModel: AddPostViewModel by viewModels()
	private var footerHeight = 0

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		viewModel.loadTagsCallback = ::loadTags
		binding = AddPostFragmentBinding.inflate(inflater, container, false)
		binding.viewModel = viewModel
		viewModel.latitude = latitude
		viewModel.longitude = longitude
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
		binding.pubblicaOnClickListener = View.OnClickListener { pubblica() }
		loadTags()
	}

	private fun loadTags() {
		val grid = binding.addPostTagsGrid
		grid.removeAllViews()
		//Tags
		for (tag in viewModel.nuovoPost.tags) {
			val tagBinding: TagItemBinding = requireContext().inflate(R.layout.tag_item)
			tagBinding.model = tag
			tagBinding.viewModel = TagItemViewModel()
			grid.addViewLast(tagBinding.root)
		}
		//AddTag
		val addTagBinding: TagItemAddBinding = requireContext().inflate(R.layout.tag_item_add)
		addTagBinding.viewModel = TagItemAddViewModel()
		addTagBinding.onClickListener = View.OnClickListener { addTags() }
		grid.addViewLast(addTagBinding.root)
	}

	private fun addTags() {

		// Recover the post's current tags
		val selectedTags = mutableSetOf<Tag>()
		viewModel.nuovoPost.tags.let { selectedTags.addAll(it) }

		// Inflate SelectTagPopupBinding
		val popupBinding =
			SelectTagPopupBinding.inflate(layoutInflater, null, false)
		popupBinding.tagsAdapter = TagsAdapter(
			tags = DataManager.tags.toList(),
			selectedTags = selectedTags
		) {
			// TagClickCallback
				tag, checked ->
			if (checked)
				tag?.let { selectedTags.add(it) }
			else
				selectedTags.remove(tag)
		}

		// Show AlertDialog
		context?.showAlertDialog(
			title = "Scegli dei tag da aggiungere",
			view = popupBinding.root,
			positiveText = "Aggiungi",
			positiveCallback = {
				viewModel.nuovoPost.tags.clear()
				viewModel.nuovoPost.tags.addAll(selectedTags)
				loadTags()
			}
		)
	}

	private fun pubblica() {
		if (viewModel.pubblica()) {
			context?.showAlertDialog(
				title = "Tutto ok",
				message = "Post pubblicato correttamente!",
				neutralCallback = {},
			)
			requireContext().getActivity<MainActivity>()?.viewModel?.currentFragment?.value =
				if (latitude != null && longitude != null) 1 else 0
		} else {

			// Scroll to the top
			binding.addPostScrollview.smoothScrollTo(
				0,
				binding.addPostScrollview.getChildAt(0).height - binding.addPostScrollview.height
			)
		}
	}

	override fun onStop() {
		super.onStop()
		viewModel.latitude = null;
		viewModel.longitude = null;
	}
}