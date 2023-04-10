package it.univpm.spottedkotlin.enums

enum class Plexuses(
	val title: String? = null,
	val locations: List<Locations> = listOf(),
) {
	INGEGNERIA(
		"Univpm - Ingegneria", listOf(
			Locations.QT_140,
			Locations.QT_150,
			Locations.QT_155,
			Locations.QT_160,
			Locations.AULE_SUD,
			Locations.PORTINERIA,
			Locations.SEGRETERIA,
			Locations.CLAB,
			Locations.DIPARTIMENTO_MATEMATICA,
			Locations.BIBLIOTECA,
		)
	),
	AGRARIA(
		"Agraria", listOf(
			Locations.AGRARIA_INGRESSO,
			Locations.AGRARIA_ATRIO,
			Locations.AGRARIA_ZONA_STUDENTI,
		)
	),
	SCIENZE(
		"Scienze", listOf(
			Locations.SCIENZE_1,
			Locations.SCIENZE_2,
			Locations.SCIENZE_3,
		)
	),
	ECONOMIA("Univpm - Economia", listOf(Locations.ECONOMIA)),
	MEDICINA("Univpm - Medicina", listOf(Locations.MEDICINA)),
	ALTRI("Altri", listOf(Locations.MENSA)),
}