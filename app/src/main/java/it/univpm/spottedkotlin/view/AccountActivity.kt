package it.univpm.spottedkotlin.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import it.univpm.spottedkotlin.databinding.ActivityAccountBinding
import it.univpm.spottedkotlin.view.fragments.AccountFragment

class AccountActivity : AppCompatActivity() {
	lateinit var binding: ActivityAccountBinding
	lateinit var fragment: AccountFragment

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityAccountBinding.inflate(layoutInflater)
		setContentView(binding.root)

		if (intent.hasExtra("userUID"))
			supportFragmentManager.commit {
				add(
					binding.accountContainer.id, AccountFragment(intent.getStringExtra("userUID"))
				)
			}
	}
}


