package it.univpm.spottedkotlin.view.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.adapter.AccountFollowingAdapter
import it.univpm.spottedkotlin.adapter.AccountPostsAdapter
import it.univpm.spottedkotlin.adapter.TagsAdapter
import it.univpm.spottedkotlin.databinding.AccountFragmentBinding
import it.univpm.spottedkotlin.databinding.SelectTagPopupBinding
import it.univpm.spottedkotlin.databinding.TagItemAddBinding
import it.univpm.spottedkotlin.databinding.TagItemBinding
import it.univpm.spottedkotlin.enums.Tags
import it.univpm.spottedkotlin.extension.function.*
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.managers.DatabaseManager
import it.univpm.spottedkotlin.managers.IOManager
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.viewmodel.AccountViewModel
import it.univpm.spottedkotlin.viewmodel.TagItemAddViewModel
import it.univpm.spottedkotlin.viewmodel.TagItemViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.notify

class AccountFragment(val uid: String? = null) : Fragment() {

	private lateinit var binding: AccountFragmentBinding
	val viewModel: AccountViewModel by viewModels()
	private val pickImageRequest = 1

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
	): View {

		binding = AccountFragmentBinding.inflate(inflater, container, false)
		binding.accountPostRecycler.layoutManager =
			LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
		binding.accountFollowingRecycler.layoutManager =
			LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

		viewModel.isCurrentUser = uid == null

		binding.modifyImage.setOnClickListener {
			openGallery()
		}
		binding.profileScrollview.setOnScrollChangeListener { _, _, scrollY, _, _ ->
			context?.getActivity<MainActivity>()?.binding?.bottomBarContainer?.translationY =
				scrollY.toFloat()
		}
		viewModel.user.observe(viewLifecycleOwner) { user ->
			"AccountFragment - viewModel.user.observe".log()
			binding.accountLoadingView.root.visibility=View.GONE
			binding.accountImageView.loadUrl(user.avatar)

			if (user.posts.isEmpty()) {
				binding.accountPostRecycler.visibility = View.GONE
				binding.noPost.visibility = View.VISIBLE
			}

			if (user.followingPosts.isEmpty()) {
				binding.accountFollowingRecycler.visibility = View.GONE
				binding.noFollowingPost.visibility = View.VISIBLE
			}

			if (user.instagramNickname == null) binding.instaName.visibility = View.GONE

			if (user.cellNumber != null) {
				binding.callButton.visibility = View.VISIBLE
				binding.callButton.setOnClickListener {
					val dialIntent = Intent(Intent.ACTION_DIAL)
					dialIntent.data = Uri.parse("tel:${user.cellNumber}")
					startActivity(dialIntent)
				}

				binding.messaggiaButton.visibility = View.VISIBLE
				binding.messaggiaButton.setOnClickListener {
					val messageIntent = Intent(Intent.ACTION_SENDTO)
					messageIntent.data = Uri.parse("smsto:${user.cellNumber}")
					startActivity(messageIntent)
				}
			} else {
				binding.callButton.visibility = View.GONE
				binding.messaggiaButton.visibility = View.GONE
			}


			if (viewModel.isCurrentUser) {
				binding.modifyImage.visibility = View.GONE
				binding.tagText.text = "Tags:"
			}

			binding.addAccountTagsGrid.removeAllViews()
			for (tag in user.tags) {
				val tagBinding: TagItemBinding = binding.root.context.inflate(R.layout.tag_item)
				tagBinding.viewModel = TagItemViewModel()
				tagBinding.model = tag
				binding.addAccountTagsGrid.addView(tagBinding.root)
			}
			if (viewModel.isCurrentUser) {
				val addTagBinding: TagItemAddBinding =
					binding.root.context.inflate(R.layout.tag_item_add)
				addTagBinding.viewModel = TagItemAddViewModel()
				addTagBinding.onClickListener = View.OnClickListener { addTags() }
				binding.addAccountTagsGrid.addView(addTagBinding.root)
			}

			binding.accountPostRecycler.adapter =
				AccountPostsAdapter(viewModel.posts.sortedByDescending { it.timestamp }.toMutableList())
			binding.accountFollowingRecycler.adapter =
				AccountFollowingAdapter(user.followingPosts.sortedByDescending { it.timestamp }.toMutableList())

		}
		binding.backButton.setOnClickListener {
			requireActivity().finish()
		}
		binding.viewModel = viewModel
		return binding.root
	}

	override fun onResume() {
		super.onResume()
		binding.accountLoadingView.loadingViewRoot.visibility=View.VISIBLE
		viewModel.initialize(uid)
		MainScope().launch {
			delay(1000)
			binding.accountLoadingView.loadingViewRoot.visibility=View.GONE
		}
	}


	// Metodo per avviare l'intent della galleria
	private fun openGallery() {
		val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
		startActivityForResult(intent, pickImageRequest)
	}

	private fun addTags() {
		viewModel.user.value?.let { user ->
			// Recover the users's current tags
			val selectedTags = mutableSetOf<Tags>()
			user.tags.let { selectedTags.addAll(it) }

			// Inflate SelectTagPopupBinding
			val popupBinding = SelectTagPopupBinding.inflate(layoutInflater, null, false)
			popupBinding.tagsAdapter = TagsAdapter(
				tags = DataManager.tags.toList(), selectedTags = selectedTags
			) {
				// TagClickCallback
					tag, checked ->
				if (checked) tag?.let { selectedTags.add(it) }
				else selectedTags.remove(tag)
			}

			// Show AlertDialog
			context?.showAlertDialog(title = "Scegli dei tag da aggiungere",
				view = popupBinding.root,
				positiveText = "Aggiungi",
				positiveCallback = {
					user.tags.clear()
					user.tags.addAll(selectedTags)
					DataManager.save(user)
					viewModel.user.value = user
				})
		}
	}

	// Gestione del risultato dell'intent
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == pickImageRequest && resultCode == Activity.RESULT_OK && data != null) {
			val selectedImageUri: Uri? = data.data
			if(selectedImageUri!=null && IOManager.getFileSize(requireContext(),selectedImageUri)>1024*1024){
				context?.toast("L'immagine non può superare 1MB")
				return
			}
			binding.accountImageView.setImageURI(selectedImageUri)
			selectedImageUri?.let {
				MainScope().launch {
					DatabaseManager.uploadImage(it)
					viewModel.initialize(uid)
				}
			}
		}
	}
}

