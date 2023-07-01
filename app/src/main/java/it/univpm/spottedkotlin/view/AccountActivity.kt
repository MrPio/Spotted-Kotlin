package it.univpm.spottedkotlin.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import it.univpm.spottedkotlin.databinding.ActivityAccountBinding
import it.univpm.spottedkotlin.view.fragments.AccountFragment

class AccountActivity : AppCompatActivity() {
	lateinit var binding: ActivityAccountBinding
	lateinit var fragment: AccountFragment
	//val viewModel: AccountViewModel by viewModels()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityAccountBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val userUID = intent.getStringExtra("userUID")
		if (userUID != null) {
			fragment = AccountFragment.newInstance(userUID)
		}

		//binding.viewModel = viewModel
		binding.executePendingBindings()

		supportFragmentManager.commit {
			add(
				binding.accountContainer.id, fragment
			)
		}
	}
}


