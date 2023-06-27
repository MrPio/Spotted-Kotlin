package it.univpm.spottedkotlin.view.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.AccountFragmentBinding
import it.univpm.spottedkotlin.enums.RemoteImages
import it.univpm.spottedkotlin.managers.AccountManager
import it.univpm.spottedkotlin.managers.BitmapManager
import it.univpm.spottedkotlin.managers.DatabaseManager
import it.univpm.spottedkotlin.viewmodel.AccountViewModel

class AccountFragment : Fragment() {
	private lateinit var binding: AccountFragmentBinding
	private val viewModel: AccountViewModel by viewModels()

	val PICK_IMAGE_REQUEST = 1

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
}