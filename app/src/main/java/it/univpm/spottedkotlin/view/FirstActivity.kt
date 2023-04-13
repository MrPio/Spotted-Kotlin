package it.univpm.spottedkotlin.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import it.univpm.spottedkotlin.databinding.FirstActivityBinding
import it.univpm.spottedkotlin.extension.function.metrics
import it.univpm.spottedkotlin.managers.DeviceManager
import it.univpm.spottedkotlin.viewmodel.FirstActivityViewModel


class FirstActivity : AppCompatActivity() {

    lateinit var binding: FirstActivityBinding
    val viewModel: FirstActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DeviceManager.displayMetrics = this.metrics()

        binding = FirstActivityBinding.inflate(layoutInflater)

        //TODO transizione a MainActivity se utente già loggato

        setContentView(binding.root)
        binding.viewModel = viewModel
    }




}