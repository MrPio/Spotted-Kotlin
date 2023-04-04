package it.univpm.spottedkotlin.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.databinding.ActivityMainBinding
import it.univpm.spottedkotlin.databinding.LoginActivityBinding
import it.univpm.spottedkotlin.extension.function.metrics
import it.univpm.spottedkotlin.managers.DeviceManager
import it.univpm.spottedkotlin.view.fragments.FirstFragment
import it.univpm.spottedkotlin.viewmodel.LoginViewModel


class FirstActivity : AppCompatActivity() {

    lateinit var binding: LoginActivityBinding
    val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DeviceManager.displayMetrics = this.metrics()

        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel
        //binding.executePendingBindings()

        /*supportFragmentManager.commit {
            add(
                binding.titleContainer.id, viewModel.fragments[0]
            )
        }*/

        /*viewModel.currentFragment.observe(this) {
            viewModel.currentFragmentChanged(binding)
        }*/


    }




}