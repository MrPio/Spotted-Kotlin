package it.univpm.spottedkotlin.enums

import android.animation.TimeInterpolator
import kotlin.math.*

enum class TimesInterpolator(val interpolator: TimeInterpolator) {
	CUBIC_IN(TimeInterpolator { it.pow(3) }),
	CUBIC_OUT(TimeInterpolator { it.pow(0.333f) }),
	LINEAR(TimeInterpolator { it}),
	ALMOST_LINEAR(TimeInterpolator { it.pow(0.7f)}),
	BELL_SIN(TimeInterpolator { sin(it * PI).toFloat() }),
}