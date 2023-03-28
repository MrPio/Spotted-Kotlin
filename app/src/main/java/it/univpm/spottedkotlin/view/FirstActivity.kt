package it.univpm.spottedkotlin.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import it.univpm.spottedkotlin.R

class FirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.first_access_page)

        startButton();
    }

    fun startButton(){
        val button =findViewById<Button>(R.id.start_button)
        button.setOnClickListener{
            startActivity(Intent(this@FirstActivity, LoginActivity::class.java))
        }

    }
}