package it.univpm.spottedkotlin.view.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.SettingsFragmentBinding
import it.univpm.spottedkotlin.view.FirstActivity
import it.univpm.spottedkotlin.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {
	private lateinit var binding: SettingsFragmentBinding
	private val viewModel: SettingsViewModel by viewModels()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?
	): View {
		binding = SettingsFragmentBinding.inflate(inflater, container, false)
		binding.viewModel = viewModel
		viewModel.gotoFirstActivityCallback = ::gotoFirstActivity
		binding.logoutButton.setOnClickListener {
			val alertDialog = AlertDialog.Builder(context)
			alertDialog.setTitle("Logout")
			alertDialog.setMessage("Sicuro di voler effettuare il logout?")


			alertDialog.setPositiveButton(
				"Si"
			) { _, _ ->
				viewModel.logout()
			}
			alertDialog.setNegativeButton(
				"Annulla", DialogInterface.OnClickListener { dialog, which ->
				})
			alertDialog.show()
		}
		return binding.root
	}

	fun gotoFirstActivity() {
		val intent = Intent(requireContext(), FirstActivity::class.java)
		startActivity(intent)
	}
}