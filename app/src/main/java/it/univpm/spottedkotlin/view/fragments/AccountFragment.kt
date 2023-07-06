package it.univpm.spottedkotlin.view.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
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
import it.univpm.spottedkotlin.model.User
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.viewmodel.AccountViewModel
import it.univpm.spottedkotlin.viewmodel.TagItemAddViewModel
import it.univpm.spottedkotlin.viewmodel.TagItemViewModel
import kotlinx.coroutines.runBlocking

class AccountFragment : Fragment() {
    private lateinit var binding: AccountFragmentBinding
    private lateinit var viewModel: AccountViewModel
    lateinit var user: User

    private lateinit var postLayoutManager: LinearLayoutManager
    private lateinit var followingLayoutManager: LinearLayoutManager
    private var accountPostsAdapter: AccountPostsAdapter = AccountPostsAdapter(mutableListOf())
    private var accountFollowingAdapter: AccountFollowingAdapter = AccountFollowingAdapter(mutableListOf())

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
                DataManager.loadUserPosts(user)
                DataManager.loadUserFollowingPosts(user)
            }
        } else {
            user = AccountManager.user
        }

        viewModel = AccountViewModel(user)

        postLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        followingLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.postsRecyclerView.layoutManager = postLayoutManager
        binding.followingRecyclerView.layoutManager = followingLayoutManager

        binding.postsAdapter = accountPostsAdapter
        binding.followingAdapter = accountFollowingAdapter

        binding.accountImageView.loadUrl(viewModel.avatar)
        initialize()
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

        if (viewModel.user.posts.isEmpty()) {
            binding.postsRecyclerView.visibility = View.GONE
            binding.noPost.visibility = View.VISIBLE
        }

        if (viewModel.user.followingPosts.isEmpty()) {
            binding.followingRecyclerView.visibility = View.GONE
            binding.noFollowingPost.visibility = View.VISIBLE
        }

        if (viewModel.nameInsta == " " || viewModel.nameInsta == null) binding.instaName.visibility =
            View.INVISIBLE

        if (viewModel.uid != AccountManager.user.uid) {
            binding.modifyImage.visibility = View.GONE
            binding.tagText.text= "Tags:"

            binding.backButton.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            if(viewModel.user.cellNumber!=null) {
                binding.chaimaButton.visibility = View.VISIBLE
                binding.chaimaButton.setOnClickListener {
                    val dialIntent = Intent(Intent.ACTION_DIAL)
                    dialIntent.data = Uri.parse("tel:${viewModel.user.cellNumber}")
                    startActivity(dialIntent)
                }

                binding.messaggiaButton.visibility = View.VISIBLE
                binding.messaggiaButton.setOnClickListener {
                    val messageIntent = Intent(Intent.ACTION_SENDTO)
                    messageIntent.data = Uri.parse("smsto:${viewModel.user.cellNumber}")
                    startActivity(messageIntent)
                }
            }

        } else {
            binding.chaimaButton.visibility = View.GONE
            binding.messaggiaButton.visibility = View.GONE
            binding.backButton.visibility = View.GONE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(this)
            .load(AccountManager.user.avatar)
            .placeholder(R.drawable.anonymous)
            .error(R.drawable.anonymous)
            .into(binding.accountImageView)

        binding.profileScrollview.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            context?.getActivity<MainActivity>()?.binding?.bottomBarContainer?.translationY =
                scrollY.toFloat()
        }

        loadTags()
        addPosts()
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
        val selectedTags = mutableSetOf<Tags>()
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
        accountPostsAdapter.updatePosts(viewModel.posts)
        accountFollowingAdapter.updatePosts(viewModel.user.followingPosts)
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

