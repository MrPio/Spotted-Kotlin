package it.univpm.spottedkotlin.view.fragments

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.SignupGeneralFragmentBinding
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.viewmodel.SignUpViewModel


class SignUpGeneralFragment : Fragment() {
    private lateinit var binding: SignupGeneralFragmentBinding
    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignupGeneralFragmentBinding.inflate(inflater, container, false)
        binding.viewModel=viewModel
        //viewModel.goToMainActivityCallback = ::goToMainActivity




        binding.doLoginText.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_signUpGeneralFragment_to_loginFragment)
        }

        binding.continueButton.setOnClickListener {
            if (binding.name.text.isEmpty()){
                binding.name.background=
                    context?.let { ContextCompat.getDrawable(it, R.drawable.text_view_border_red) }
                binding.optionalFieldsText.setTextColor(Color.RED)
                binding.optionalFieldsText.text="Campi obbligatori"
            }
            else binding.name.background=
                context?.let { ContextCompat.getDrawable(it, R.drawable.text_view_border_grey) }
            if (binding.surname.text.isEmpty()){
                binding.surname.background=
                    context?.let { ContextCompat.getDrawable(it, R.drawable.text_view_border_red) }
                binding.optionalFieldsText.setTextColor(Color.RED)
                binding.optionalFieldsText.text="Campi obbligatori"
            }
            else binding.surname.background=
                context?.let { ContextCompat.getDrawable(it, R.drawable.text_view_border_grey) }

            if (!binding.name.text.isEmpty() && !binding.surname.text.isEmpty()) {
                binding.root.findNavController().navigate(R.id.action_signUpGeneralFragment_to_signUpFragment)
            }

        }

        binding.googleSignupButton.setOnClickListener {
          //google button
        }

        return binding.root
    }





//                binding.male ->
//                    if (checked) {
//                        view.setBackgroundColor(Color.RED)
//                    }
//                    else view.setBackgroundColor(Color.BLACK)
//                binding.female ->
//                    if (checked) {
//                        view.setBackgroundColor(Color.RED)
//                    }
//                    else view.setBackgroundColor(Color.BLACK)
//                binding.other ->
//                    if (checked) {
//                        view.setBackgroundColor(Color.RED)
//                    }
//                    else view.setBackgroundColor(Color.BLACK)


    private fun goToMainActivity(){

        //binding.root.findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        requireActivity().runOnUiThread { startActivity(Intent(activity, MainActivity::class.java)) }
    }

}