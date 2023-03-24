package it.univpm.spottedkotlin.enums

enum class Gender {
	MALE {
		override val icon = "\uDB80\uDE9D"
	},
	FEMALE {
		override val icon = "\uDB80\uDE9C"
	};

	open val icon: String = ""
}