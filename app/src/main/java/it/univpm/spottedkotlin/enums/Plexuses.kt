package it.univpm.spottedkotlin.enums

import android.location.Location

enum class Plexuses(
	val title: String? = null,
	val locations: List<Locations> = listOf(),
) {
	INGEGNERIA(
		"Univpm - Ingegneria", listOf(
			Locations.INGEGNERIA,
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
			Locations.AGRARIA,
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
	ECONOMIA(
		"Univpm - Economia",
		listOf(
			Locations.ECONOMIA,
			Locations.ECONOMIA_AULA_A,
			Locations.ECONOMIA_AULA_C,
			Locations.ECONOMIA_AULA_T_27,
			Locations.ECONOMIA_AULA_T_PICCOLA,
			Locations.ECONOMIA_AULE_DOTTORATO,
			Locations.ECONOMIA_BIBLIOTECA,
			Locations.ECONOMIA_CHIOSTRO,
			Locations.ECONOMIA_SALA_LETTURA,
			Locations.ECONOMIA_SBT,
			Locations.ECONOMIA_SEGRETERIA_STUDENTI
		)
	),

	MEDICINA_MURRI(
		"Univpm - Medicina Murri",
		listOf(
			Locations.MEDICINA_MURRI,
			Locations.MEDICINA_MURRI_AULA_P1,
			Locations.MEDICINA_MURRI_AULA_R,
			Locations.MEDICINA_MURRI_PIANO_1,
			Locations.MEDICINA_MURRI_PIANO_2,
			Locations.MEDICINA_MURRI_PIANO_3,
			Locations.MEDICINA_MURRI_PIANO_4,
			Locations.MEDICINA_MURRI_PIANO_TERRA


		)
	),
	MEDICINA_EUSTACCHIO(
		"Univpm - Medicina Eustacchio",
		listOf(
			Locations.MEDICINA_EUSTACCHIO,
			Locations.MEDICINA_EUSTACCHIO_ATELIER,
			Locations.MEDICINA_EUSTACCHIO_AULA_D,
			Locations.MEDICINA_EUSTACCHIO_AULE_STUDIO,
			Locations.MEDICINA_EUSTACCHIO_BIBLIOTECA,
			Locations.MEDICINA_EUSTACCHIO_LAURE_TRIENNALI,
			Locations.MEDICINA_EUSTACCHIO_PIANO_1,
			Locations.MEDICINA_EUSTACCHIO_PIANO_2,
			Locations.MEDICINA_EUSTACCHIO_PIANO_TERRA,
			Locations.MEDICINA_EUSTACCHIO_SEGRETERIA,
		)
	),
	ANCONA(
		"Ancona",
		listOf(
			Locations.ANCONA,
			Locations.ANCONA_CAVOUR,
			Locations.ANCONA_CITTADELLA,
			Locations.ANCONA_MOLE,
			Locations.ANCONA_PASSETTO,
			Locations.ANCONA_PIAZZA_DEL_PAPA,
			Locations.ANCONA_SAN_CIRIACO,
			Locations.ANCONA_STAZIONE,
			Locations.ANCONA_UGO_BASSI
		)
	),
	ALTRI(
		"Altri",
		listOf(
			Locations.MENSA_INGEGNERIA,
			Locations.MENSA_ECONOMIA,
		)
	),
}