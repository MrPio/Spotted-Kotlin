package it.univpm.spottedkotlin.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import it.univpm.spottedkotlin.adapter.AddPostTagsAdapter
import it.univpm.spottedkotlin.adapter.TagsAdapter
import it.univpm.spottedkotlin.databinding.AddPostFragmentBinding
import it.univpm.spottedkotlin.databinding.SelectTagPopupBinding
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.extension.function.fromDp
import it.univpm.spottedkotlin.extension.function.getActivity
import it.univpm.spottedkotlin.extension.function.setHeight
import it.univpm.spottedkotlin.model.Tag
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.viewmodel.AddPostViewModel
import kotlin.math.max


class AddPostFragment : Fragment() {
	private lateinit var binding: AddPostFragmentBinding
	private val viewModel: AddPostViewModel by viewModels()
	private var footerHeight = 0
	private val adapter = AddPostTagsAdapter(
		mutableListOf(
			Tag("fem", Gender.FEMALE.icon),
			Tag("mas", Gender.MALE.icon),
		),
		this::chooseTag

	)

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = AddPostFragmentBinding.inflate(inflater, container, false)
		binding.viewModel = viewModel
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

		binding.tagsAdapter = adapter
	}

	private fun chooseTag() {
		val popupBinding = SelectTagPopupBinding.inflate(layoutInflater, null, false)
		popupBinding.tagsAdapter = TagsAdapter(
			mutableListOf(
				Tag("fem", Gender.FEMALE.icon),
				Tag("mas", Gender.MALE.icon),
				Tag("fem", Gender.FEMALE.icon),
				Tag("mas", Gender.MALE.icon),
				Tag("fem", Gender.FEMALE.icon),
				Tag("mas", Gender.MALE.icon),
				Tag("fem", Gender.FEMALE.icon),
				Tag("mas", Gender.MALE.icon),
				Tag("fem", Gender.FEMALE.icon),
				Tag("mas", Gender.MALE.icon),
			),
		)
		popupBinding.selectTagPopupGridView.onItemClickListener =
			OnItemClickListener { _, v, position, _ ->
			}

		val builder = AlertDialog.Builder(requireContext())
		builder.setTitle("Scegli dei tag da aggiungere")
		builder.setView(popupBinding.root)
		builder.setPositiveButton("OK") { dialog, which -> }
		builder.setNegativeButton("Cancel") { dialog, which -> }

		val dialog = builder.create()
		dialog.show()
	}
}