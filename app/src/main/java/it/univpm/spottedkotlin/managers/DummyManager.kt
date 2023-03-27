package it.univpm.spottedkotlin.managers

import android.text.format.DateUtils
import it.univpm.spottedkotlin.enums.Gender
import it.univpm.spottedkotlin.enums.Locations
import it.univpm.spottedkotlin.extension.function.addDays
import it.univpm.spottedkotlin.model.Post
import java.util.Calendar

object DummyManager {
	fun generatePosts(limit:Int=15){
		val posts= mutableListOf<Post>()
		for (i in 0..limit){
			posts.add(
				Post(
					authorUID = RandomManager.getRandomString(28),
					location = Locations.values().random(),
					gender = Gender.values().random(),
					Calendar.getInstance().time.addDays((-355..0).random())
				)
			)
		}
	}
}