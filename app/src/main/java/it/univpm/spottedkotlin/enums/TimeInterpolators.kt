package it.univpm.spottedkotlin.enums

import android.animation.TimeInterpolator
import kotlin.math.PI
import kotlin.math.pow
import kotlin.math.sin

enum class TimesInterpolator(val interpolator: TimeInterpolator) {
	CUBIC_IN(TimeInterpolator { it.pow(3) }),
	CUBIC_OUT(TimeInterpolator { it.pow(1f / 3) }),
	BELL_SIN(TimeInterpolator { sin(it * PI).toFloat() }),
}