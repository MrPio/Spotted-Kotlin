package it.univpm.spottedkotlin.view.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.adapter.AccountPostsAdapter
import it.univpm.spottedkotlin.adapter.TagsAdapter
import it.univpm.spottedkotlin.databinding.AccountFragmentBinding
import it.univpm.spottedkotlin.databinding.SelectTagPopupBinding
import it.univpm.spottedkotlin.databinding.TagItemAddBinding
import it.univpm.spottedkotlin.databinding.TagItemBinding
import it.univpm.spottedkotlin.extension.function.addViewLast
import it.univpm.spottedkotlin.extension.function.inflate
import it.univpm.spottedkotlin.extension.function.loadUrl
import it.univpm.spottedkotlin.extension.function.showAlertDialog
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.DataManager
import it.univpm.spottedkotlin.managers.DatabaseManager
import it.univpm.spottedkotlin.managers.LogManager.TAG
import it.univpm.spottedkotlin.model.Tag
import it.univpm.spottedkotlin.model.User
import it.univpm.spottedkotlin.viewmodel.AccountViewModel
import it.univpm.spottedkotlin.viewmodel.TagItemAddViewModel
import it.univpm.spottedkotlin.viewmodel.TagItemViewModel
import kotlinx.coroutines.runBlocking

class AccountFragment : Fragment() {
    private lateinit var binding: AccountFragmentBinding
    private lateinit var viewModel: AccountViewModel
    lateinit var user: User

    private lateinit var layoutManager: LinearLayoutManager
    private var accountPostsAdapter: AccountPostsAdapter = AccountPostsAdapter(mutableListOf())

    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = AccountFragmentBinding.inflate(inflater, container, false)
        binding.modifyImage.setOnClickListener { view ->
            openGallery()
        }


        val uid = arguments?.getString("uid")
        if (uid != null) {
            runBlocking {
                user = DataManager.loadUser(uid)
            }
        } else {
            user = AccountManager.user
        }
        viewModel = AccountViewModel(user)
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.postsRecyclerView.layoutManager = layoutManager
        binding.postsAdapter = accountPostsAdapter

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initialize()
    }

    private fun initialize() {
        viewModel = AccountViewModel(user)
        binding.viewModel = viewModel
        binding.accountImageView.loadUrl(viewModel.avatar)
        if (viewModel.uid != AccountManager.user.uid) {
            binding.modifyImage.visibility = View.GONE
            binding.chaimaButton.visibility = View.VISIBLE
            binding.messaggiaButton.visibility = View.VISIBLE
            binding.tagText.text= "Tags:"
        } else {
            binding.chaimaButton.visibility = View.GONE
            binding.messaggiaButton.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO meglio nell' onCreateView ???
        Glide.with(this)
            .load(AccountManager.user.avatar)
            .placeholder(R.drawable.anonymous)
            .error(R.drawable.anonymous)
            .into(binding.accountImageView)


        loadTags()
        addPosts()

        if (viewModel.nameInsta == " " || viewModel.nameInsta == null) binding.instaName.visibility =
            View.INVISIBLE
        if (viewModel.uid != AccountManager.user.uid) binding.modifyImage.visibility = View.GONE
        if (viewModel.user.posts.isEmpty()) {
            binding.postsRecyclerView.visibility = View.GONE
            binding.noPost.visibility = View.VISIBLE
        }
        initialize()
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
            initialize()
        }
    }

    fun loadTags() {
        for (tag in viewModel.tags) {
            val tagBinding: TagItemBinding = binding.root.context.inflate(R.layout.tag_item)
            tagBinding.viewModel = TagItemViewModel()
            tagBinding.model = tag
            binding.addAccountTagsGrid.addView(tagBinding.root)
        }
        if (viewModel.uid == AccountManager.user.uid || binding.addAccountTagsGrid.isEmpty()) {
            val addTagBinding: TagItemAddBinding = binding.root.context.inflate(R.layout.tag_item_add)
            addTagBinding.viewModel = TagItemAddViewModel()
            addTagBinding.onClickListener = View.OnClickListener { addTags() }
            binding.addAccountTagsGrid.addView(addTagBinding.root)
        }

    }

    private fun addTags() {

        // Recover the users's current tags
        val selectedTags = mutableSetOf<Tag>()
        viewModel.user.tags.let { selectedTags.addAll(it) }

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
                binding.addAccountTagsGrid.removeAllViews()
                viewModel.user.tags.clear()
                AccountManager.user.tags.addAll(selectedTags)
                loadTags()
                DataManager.save(viewModel.user)
            }
        )
    }

    fun addPosts() {
        accountPostsAdapter.updatePosts(viewModel.user.posts)
    }


    companion object {
        fun newInstance(uid: String): AccountFragment {
            val fragment = AccountFragment()
            val args = Bundle().apply {
                putString("uid", uid)
            }
            fragment.arguments = args
            return fragment
        }
    }
}

