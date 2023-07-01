package it.univpm.spottedkotlin.view.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.adapter.PostsAdapter
import it.univpm.spottedkotlin.databinding.AccountFragmentBinding
import it.univpm.spottedkotlin.databinding.TagItemAddBinding
import it.univpm.spottedkotlin.databinding.TagItemBinding
import it.univpm.spottedkotlin.extension.function.inflate
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DatabaseManager
import it.univpm.spottedkotlin.viewmodel.AccountViewModel
import it.univpm.spottedkotlin.viewmodel.TagItemViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class AccountFragment : Fragment() {
	private lateinit var binding: AccountFragmentBinding
	private val viewModel: AccountViewModel by viewModels()

	private lateinit var layoutManager: LinearLayoutManager
	private var postsAdapter: PostsAdapter = PostsAdapter(mutableListOf())

	private val PICK_IMAGE_REQUEST = 1

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
	}
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {

		binding = AccountFragmentBinding.inflate(inflater, container, false)
		binding.viewModel=viewModel
		binding.modifyImage.setOnClickListener{ view ->
			openGallery()
		}

//		val uid = arguments?.getString("uid")
//		MainScope().launch {
//			if (uid != null) {
//				viewModel.setUser(uid)
//			}
//		}

		layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

		binding.postsRecyclerView.layoutManager = layoutManager
		binding.postsAdapter = postsAdapter

		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		//TODO meglio nell' onCreateView ???
		Glide.with(this)
			.load(AccountManager.user.avatar)
			.placeholder(R.drawable.anonymous)
			.error(R.drawable.anonymous)
			.into(binding.accountImageView)

		addTags()
		addPosts()

		if(viewModel.nameInsta == " " || viewModel.nameInsta == null) binding.instaName.visibility= View.INVISIBLE
		if(viewModel.userPosts.isEmpty()){
			binding.postsRecyclerView.visibility= View.GONE
			binding.noPost.visibility= View.VISIBLE
		}

		print(viewModel.userPosts+"\n\n\n"+viewModel.userPosts.size+"\n\n")
	}

	// Metodo per avviare l'intent della galleria
	private fun openGallery() {
		val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
		startActivityForResult(intent, PICK_IMAGE_REQUEST)
	}

	// Gestione del risultato dell'intent
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
			val selectedImageUri: Uri? = data.data
			binding.accountImageView.setImageURI(selectedImageUri)
			if (selectedImageUri != null) {
				DatabaseManager.loadImg(selectedImageUri)
			}
		}
	}

	fun addTags(){
		for(tag in viewModel.tags){
			val tagBinding: TagItemBinding = binding.root.context.inflate(R.layout.tag_item)
			tagBinding.viewModel = TagItemViewModel()
			tagBinding.model = tag
			binding.addAccountTagsGrid.addView(tagBinding.root)
		}
		val tagBinding: TagItemAddBinding = binding.root.context.inflate(R.layout.tag_item_add)
		binding.addAccountTagsGrid.addView(tagBinding.root)
	}

	 fun addPosts(){
		postsAdapter.updatePosts(viewModel.userPosts)
	}
}