package it.univpm.spottedkotlin.extension.function

import android.content.res.Resources
import java.io.InputStream

fun Resources.loadTxt(res: Int): String? {
	return try {
		val in_s: InputStream = this.openRawResource(res)
		val b = ByteArray(in_s.available())
		in_s.read(b)
		String(b)
	} catch (_: Exception) {
		null
	}
}