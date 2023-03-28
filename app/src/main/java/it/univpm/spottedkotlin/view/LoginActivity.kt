package it.univpm.spottedkotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import it.univpm.spottedkotlin.R

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_access_login)
    }
}