package it.univpm.spottedkotlin.enums

enum class Gender(val title: String, val icon: String) {
	MALE("Ragazzo", "\uDB80\uDE9D"),
	FEMALE("Ragazza", "\uDB80\uDE9C"),
	OTHER("Altro" , listOf<String>("\uDB80\uDE9D","\uDB80\uDE9C").random())
}