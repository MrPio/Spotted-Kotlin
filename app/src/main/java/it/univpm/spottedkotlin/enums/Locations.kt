package it.univpm.spottedkotlin.enums

enum class Locations(
	val title: String? = null,
	private val _imageUrl: String? = null,
	val latitude: Double,
	val longitude: Double,
) {
	//=== PLESSI ======================================================
	INGEGNERIA(
		"Univpm - Ingegneria", RemoteImages.TORRE.url,
		latitude = 43.58711315929607,
		longitude = 13.516095990145402
	),
	AGRARIA(
		"Agraria",
		latitude = 43.588572328368336,
		longitude = 13.51516842842102
	),
	SCIENZE(
		"Scienze",
		RemoteImages.SCIENZE_1.url,
		latitude = 43.58538613538574,
		longitude = 13.515731692314148
	),
	MENSA_INGEGNERIA(
		"Mensa Ingegneria",
		latitude = 43.59030329299805,
		longitude = 13.514167964458466
	),
	MENSA_ECONOMIA(
		"Mensa Economia",
		latitude = 43.617823032208484,
		longitude = 13.51831667251244
	),
	MEDICINA_MURRI(
		"Univpm - Medicina Murri",
		latitude = 43.60370454251944,
		longitude = 13.456659811463965
	),
	MEDICINA_EUSTACCHIO(
		"Univpm - Medicina Eustacchio",
		latitude = 43.60348052321864,
		longitude = 13.455408164597543
	),
	ECONOMIA(
		"Univpm - Economia",
		latitude = 43.620468639014845,
		longitude = 13.517400449787345
	),
	ANCONA(
		"Ancona",
		latitude = 43.6100,
		longitude = 13.5134
	),
	//=================================================================

	//INGEGNERIA
	QT_140(
		"Quota 140",
		latitude = 43.58781853696623,
		longitude = 13.516844809055328
	),
	QT_150(
		"Quota 150",
		latitude = 43.5874552372234,
		longitude = 13.51694941520691
	),
	QT_155(
		"Quota 155",
		latitude = 43.587056964932366,
		longitude = 13.516828715801239
	),
	QT_160(
		"Quota 160",
		latitude = 43.58671891711529,
		longitude = 13.516501486301422
	),
	AULE_SUD(
		"Aule Sud", RemoteImages.AULE_SUD_ATRIO.url,
		latitude = 43.58600201632468,
		longitude = 13.513856828212738
	),
	PORTINERIA(
		"Portineria",
		latitude = 43.58689765527809,
		longitude = 13.516793847084045
	),
	SEGRETERIA(
		"Segreteria",
		latitude = 43.586746116435165,
		longitude = 13.515141606330872
	),
	CLAB(
		"Clab",
		latitude = 43.58732895605058,
		longitude = 13.516689240932465
	),
	DIPARTIMENTO_MATEMATICA(
		"Dip. Matematica/Copisteria",
		latitude = 43.58714439078296,
		longitude = 13.515782654285431
	),
	BIBLIOTECA(
		"Biblioteca",
		latitude = 43.58696565335283,
		longitude = 13.516058921813965
	),

	//AGRARIA
	AGRARIA_ATRIO(
		"Atrio",
		latitude = 43.588572328368336,
		longitude = 13.51516842842102
	),
	AGRARIA_INGRESSO(
		"Ingresso", RemoteImages.AGRARIA.url,
		latitude = 43.58874134730976,
		longitude = 13.515340089797974
	),
	AGRARIA_ZONA_STUDENTI(
		"Zona studenti",
		latitude = 43.58843050751106,
		longitude = 13.515128195285797
	),

	//SCIENZE
	SCIENZE_1(
		"Scienze 1",
		latitude = 43.58508304970802,
		longitude = 13.516235947608948
	),
	SCIENZE_2(
		"Scienze 2",
		latitude = 43.58509276401623,
		longitude = 13.515581488609314
	),
	SCIENZE_3(
		"Scienze 3",
		latitude = 43.58511219262796,
		longitude = 13.514996767044067
	),

	//ECONOMIA
	ECONOMIA_AULA_A(
		"Aula A",
		latitude = 43.620468639014845,
		longitude = 13.517400449787345
	),
	ECONOMIA_AULA_C(
		"Aula C",
		latitude = 43.620468639014845,
		longitude = 13.517400449787345
	),
	ECONOMIA_AULA_T_27(
		"Aula T 27",
		latitude = 43.620468639014845,
		longitude = 13.517400449787345
	),
	ECONOMIA_AULA_T_PICCOLA(
		"Aula T piccola",
		latitude = 43.620468639014845,
		longitude = 13.517400449787345
	),
	ECONOMIA_AULE_DOTTORATO(
		"Aule Dottorato",
		latitude = 43.620468639014845,
		longitude = 13.517400449787345
	),
	ECONOMIA_BIBLIOTECA(
		"Biblioteca",
		latitude = 43.620468639014845,
		longitude = 13.517400449787345
	),
	ECONOMIA_CHIOSTRO(
		"Chiostro",
		latitude = 43.620468639014845,
		longitude = 13.517400449787345
	),
	ECONOMIA_SALA_LETTURA(
		"Sala lettura",
		latitude = 43.620468639014845,
		longitude = 13.517400449787345
	),
	ECONOMIA_SBT(
		"San Benedetto del Tronto",
		latitude = 43.620468639014845,
		longitude = 13.517400449787345
	),
	ECONOMIA_SEGRETERIA_STUDENTI(
		"Segreteria",
		latitude = 43.620468639014845,
		longitude = 13.517400449787345
	),

	//ANCONA
	ANCONA_CAVOUR(
		"Centro",
		latitude = 43.61704344736856,
		longitude = 13.516474621147154
	),
	ANCONA_CITTADELLA(
		"Cittadella",
		latitude = 43.61301300566302,
		longitude = 13.510502994981039
	),
	ANCONA_MOLE(
		"Mole/Archi",
		latitude = 43.61433136268958,
		longitude = 13.503850824149225
	),
	ANCONA_PASSETTO(
		"Passetto",
		latitude = 43.6149483616053,
		longitude = 13.533580203286135
	),
	ANCONA_PIAZZA_DEL_PAPA(
		"Piazza del Papa",
		latitude = 43.619404770090064,
		longitude = 13.51202962220421
	),
	ANCONA_SAN_CIRIACO(
		"Cattedrale San Ciriaco",
		latitude = 43.625293439823004,
		longitude = 13.510373821263812
	),
	ANCONA_STAZIONE(
		"Stazione FS",
		latitude = 43.60749068440513,
		longitude = 13.498050866907818
	),
	ANCONA_UGO_BASSI(
		"Piazza Ugo Bassi",
		latitude = 43.603492044747306,
		longitude = 13.5073462310487
	),

	//MEDICINA_MURRI_AULA_P1
	MEDICINA_MURRI_AULA_P1(
		"Aula P1",
		latitude = 43.60370454251944,
		longitude = 13.456659811463965
	),
	MEDICINA_MURRI_AULA_R(
		"Aula R",
		latitude = 43.60370454251944,
		longitude = 13.456659811463965
	),
	MEDICINA_MURRI_PIANO_TERRA(
		"Piano terra",
		latitude = 43.60370454251944,
		longitude = 13.456659811463965
	),
	MEDICINA_MURRI_PIANO_1(
		"Piano 1",
		latitude = 43.60370454251944,
		longitude = 13.456659811463965
	),
	MEDICINA_MURRI_PIANO_2(
		"Piano 2",
		latitude = 43.60370454251944,
		longitude = 13.456659811463965
	),
	MEDICINA_MURRI_PIANO_3(
		"Piano 3",
		latitude = 43.60370454251944,
		longitude = 13.456659811463965
	),
	MEDICINA_MURRI_PIANO_4(
		"Piano 4",
		latitude = 43.60370454251944,
		longitude = 13.456659811463965
	),

	//MEDICINA_EUSTACCHIO
	MEDICINA_EUSTACCHIO_ATELIER(
		"Atelier",
		latitude = 43.60348052321864,
		longitude = 13.455408164597543
	),
	MEDICINA_EUSTACCHIO_AULA_D(
		"Aula D",
		latitude = 43.60348052321864,
		longitude = 13.455408164597543
	),
	MEDICINA_EUSTACCHIO_AULE_STUDIO(
		"Aule studio",
		latitude = 43.60348052321864,
		longitude = 13.455408164597543
	),
	MEDICINA_EUSTACCHIO_BIBLIOTECA(
		"Biblioteca",
		latitude = 43.60348052321864,
		longitude = 13.455408164597543
	),
	MEDICINA_EUSTACCHIO_LAURE_TRIENNALI(
		"Laure Triennali",
		latitude = 43.60348052321864,
		longitude = 13.455408164597543
	),
	MEDICINA_EUSTACCHIO_PIANO_TERRA(
		"Piano terra",
		latitude = 43.60348052321864,
		longitude = 13.455408164597543
	),
	MEDICINA_EUSTACCHIO_PIANO_1(
		"Piano 1",
		latitude = 43.60348052321864,
		longitude = 13.455408164597543
	),
	MEDICINA_EUSTACCHIO_PIANO_2(
		"Piano 2",
		latitude = 43.60348052321864,
		longitude = 13.455408164597543
	),
	MEDICINA_EUSTACCHIO_SEGRETERIA(
		"Segreteria",
		latitude = 43.60348052321864,
		longitude = 13.455408164597543
	);


	val imageUrl get() = _imageUrl ?: RemoteImages.valueOf(this.name).url
	val plexus get() = Plexuses.values().find { it.locations.contains(this) }
}