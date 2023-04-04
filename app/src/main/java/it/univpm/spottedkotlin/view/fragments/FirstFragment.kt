package it.univpm.spottedkotlin.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.adapter.HomePostsAdapter
import it.univpm.spottedkotlin.databinding.FirstFragmentBinding
import it.univpm.spottedkotlin.extension.function.getActivity
import it.univpm.spottedkotlin.generated.callback.OnClickListener
import it.univpm.spottedkotlin.view.FirstActivity
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.viewmodel.LoginViewModel


class FirstFragment : Fragment() {
    private lateinit var binding: FirstFragmentBinding
    private val viewModel: LoginViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FirstFragmentBinding.inflate(inflater, container, false)

        binding.startButton.setOnClickListener(){
                binding.root.findNavController().navigate(R.id.action_firstFragment_to_loginFragment)
        }
        return binding.root

    }







}