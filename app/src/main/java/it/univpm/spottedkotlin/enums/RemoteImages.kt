package it.univpm.spottedkotlin.enums

import android.graphics.Bitmap
import it.univpm.spottedkotlin.managers.BitmapManager

enum class RemoteImages(private val _url: String = "") {

	//Misc
	TOP_SPRITE_001("top_sprite_001.png"),
	ANONNYMOUS("anonymous.png"),
	AVATAR("avatar.png"),
	AVATAR_1, AVATAR_2, AVATAR_3, AVATAR_4, AVATAR_5, AVATAR_6, AVATAR_7, AVATAR_8,
	AVATAR_9, AVATAR_10, AVATAR_11, AVATAR_12, AVATAR_13, AVATAR_14, AVATAR_15, AVATAR_16,
	AVATAR_17, AVATAR_18, AVATAR_19, AVATAR_20, AVATAR_21, AVATAR_22, AVATAR_23, AVATAR_24,
	AVATAR_25, AVATAR_26, AVATAR_27, AVATAR_28, AVATAR_29, AVATAR_30, AVATAR_31,
	MAP_MARKER("map_marker.png"),
	CIRCLE_WHITE("circle_white.png"),

	//Locations
	QT_140("uni_140.jpg"), QT_150, QT_155, QT_160("uni_160.jpg"),
	SCIENZE_1, SCIENZE_2, SCIENZE_3,
	PORTINERIA, SEGRETERIA,
	CLAB, DIPARTIMENTO_MATEMATICA, BIBLIOTECA,
	MENSA,
	MEDICINA,
	AULE_SUD, AULE_SUD_ATRIO,
	AGRARIA_ATRIO, AGRARIA_ZONA_STUDENTI, AGRARIA,
	TORRE,
	ECONOMIA;

	val url
		get() = "https://firebasestorage.googleapis.com/v0/b/spotted-f3589.appspot.com/o/src%2F" +
				"${_url.ifEmpty { this.name.lowercase() + if (this.name.contains("AVATAR")) ".png" else ".jpg" }}?alt=media"

	fun load(): Bitmap = BitmapManager.load(this)
}