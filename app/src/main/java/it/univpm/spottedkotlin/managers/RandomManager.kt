package it.univpm.spottedkotlin.managers

object RandomManager {
	fun getRandomString(length: Int) : String {
		val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
		return (1..length)
			.map { allowedChars.random() }
			.joinToString("")
	}
}