package it.univpm.spottedkotlin.view.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.SignupGeneralFragmentBinding
import it.univpm.spottedkotlin.enums.Gender
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
        viewModel.goToSignUpFragmentCallback = ::goToSignUpFragmentCallback
        binding.viewModel= viewModel


        binding.doLoginText.setOnClickListener {
            binding.root.findNavController().navigate(R.id.action_signUpGeneralFragment_to_loginFragment)
        }

        binding.googleSignupButton.setOnClickListener {
          //google button
        }

        binding.male.setOnClickListener(){
            viewModel.gender = Gender.MALE
        }

        binding.female.setOnClickListener(){
            viewModel.gender = Gender.FEMALE
        }

        binding.other.setOnClickListener(){
            viewModel.gender = Gender.OTHER
        }





        return binding.root
    }

    private fun goToSignUpFragmentCallback(){
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
}