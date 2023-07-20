package it.univpm.spottedkotlin.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import it.univpm.spottedkotlin.databinding.ActivityAccountBinding
import it.univpm.spottedkotlin.databinding.ActivitySettingsBinding
import it.univpm.spottedkotlin.view.fragments.AccountFragment
import it.univpm.spottedkotlin.view.fragments.SettingsFragment

class SettingsActivity : AppCompatActivity() {
	lateinit var binding: ActivitySettingsBinding
	lateinit var fragment: SettingsFragment

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivitySettingsBinding.inflate(layoutInflater)
		fragment=SettingsFragment()
		setContentView(binding.root)

			supportFragmentManager.commit {
				add(
					binding.settingsActivityContainer.id, fragment
				)
			}
	}
}


