package it.univpm.spottedkotlin.enums

enum class Locations(val title: String? = null, private val _imageUrl: String? = null) {
	//=== PLESSI ======================================================
	INGEGNERIA("Univpm - Ingegneria", RemoteImages.TORRE.url),
	AGRARIA("Agraria"),
	SCIENZE("Scienze", RemoteImages.SCIENZE_1.url),
	MENSA_INGEGNERIA("Mensa Ingegneria"),
	MENSA_ECONOMIA("Mensa Economia"),
	MEDICINA("Univpm - Medicina"),
	ECONOMIA("Univpm - Economia"),
	ANCONA("Ancona"),
	//=================================================================

	//INGEGNERIA
	QT_140("Quota 140"),
	QT_150("Quota 150"),
	QT_155("Quota 155"),
	QT_160("Quota 160"),
	AULE_SUD("Aule Sud", RemoteImages.AULE_SUD_ATRIO.url),
	PORTINERIA("Portineria"),
	SEGRETERIA("Segreteria"),
	CLAB("Clab"),
	DIPARTIMENTO_MATEMATICA("Dip. Matematica/Copisteria"),
	BIBLIOTECA("Biblioteca"),

	//AGRARIA
	AGRARIA_ATRIO("Atrio"),
	AGRARIA_INGRESSO("Ingresso", RemoteImages.AGRARIA.url),
	AGRARIA_ZONA_STUDENTI("Zona studenti"),

	//SCIENZE
	SCIENZE_1("Scienze 1"),
	SCIENZE_2("Scienze 2"),
	SCIENZE_3("Scienze 3"),

	//ECONOMIA
	ECONOMIA_AULA_A("Aula A"),
	ECONOMIA_AULA_C("Aula C"),
	ECONOMIA_AULA_T_27("Aula T 27"),
	ECONOMIA_AULA_T_PICCOLA("Aula T piccola"),
	ECONOMIA_AULE_DOTTORATO("Aule Dottorato"),
	ECONOMIA_BIBLIOTECA("Biblioteca"),
	ECONOMIA_CHIOSTRO("Chiostro"),
	ECONOMIA_SALA_LETTURA("Sala lettura"),
	ECONOMIA_SBT("San Benedetto del Tronto"),
	ECONOMIA_SEGRETERIA_STUDENTI("Segreteria"),

	//ANCONA
	ANCONA_CAVOUR("Centro"),
	ANCONA_CITTADELLA("Cittadella"),
	ANCONA_MOLE("Mole/Archi"),
	ANCONA_PASSETTO("Passetto"),
	ANCONA_PIAZZA_DEL_PAPA("Piazza del Papa"),
	ANCONA_SAN_CIRIACO("Cattedrale San Ciriaco"),
	ANCONA_STAZIONE("Stazione FS"),
	ANCONA_UGO_BASSI("Piazza Ugo Bassi");

	val imageUrl get() = _imageUrl ?: RemoteImages.valueOf(this.name).url
	val plexus get() = Plexuses.values().find { it.locations.contains(this) }
}