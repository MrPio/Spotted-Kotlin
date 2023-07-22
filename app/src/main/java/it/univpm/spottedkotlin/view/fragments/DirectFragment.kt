package it.univpm.spottedkotlin.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.adapter.SearchUserAdapter
import it.univpm.spottedkotlin.databinding.ChatCardBinding
import it.univpm.spottedkotlin.databinding.DirectFragmentBinding
import it.univpm.spottedkotlin.databinding.EmojiItemBinding
import it.univpm.spottedkotlin.databinding.SearchUserFragmentBinding
import it.univpm.spottedkotlin.extension.function.getActivity
import it.univpm.spottedkotlin.extension.function.goto
import it.univpm.spottedkotlin.extension.function.inflate
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.view.CommentsActivity
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.viewmodel.DirectViewModel
import it.univpm.spottedkotlin.viewmodel.SearchUserViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DirectFragment : Fragment() {

	private lateinit var binding: DirectFragmentBinding
	private lateinit var chatsLayoutManager: LinearLayoutManager
	val viewModel: DirectViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = DirectFragmentBinding.inflate(inflater, container, false)
		chatsLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

		binding.viewModel = viewModel
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		// AddChat
		binding.directNewChat.setOnClickListener {
			(requireActivity() as MainActivity).viewModel.currentFragment.value = 3
		}

		// Hide the BottomBar with scroll
		binding.directScrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
			context?.getActivity<MainActivity>()?.binding?.bottomBarContainer?.translationY =
				scrollY.toFloat()
		}

		// Populate the chats inside the ViewGroup
		viewModel.chats.observe(requireActivity()) { chats ->
			binding.directContent.removeAllViews()
			chats.forEach { chat ->
				val chatView = requireContext().inflate<ChatCardBinding>(R.layout.chat_card)
				val otherUser = chat.users.find { it.uid != AccountManager.user.uid }
				chatView.modelChat = chat
				chatView.modelUser = otherUser
				chatView.chatCardHighlightView.setOnClickListener {
					activity?.goto<CommentsActivity>(mapOf("chatUserUID" to otherUser?.uid))
				}
				binding.directContent.addView(chatView.root)
			}
		}
	}

	override fun onResume() {
		super.onResume()
		viewModel.initialize()
	}
}