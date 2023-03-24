package it.univpm.spottedkotlin.managers

import android.animation.ValueAnimator
import it.univpm.spottedkotlin.enums.TimesInterpolator

object AnimationManager {
	fun animate(
		start: Number,
		end: Number,
		interpolator: TimesInterpolator,
		duration:Long=250L,
		update: (animator: ValueAnimator) -> Unit
	) {
		ValueAnimator.ofFloat(start.toFloat(), end.toFloat())
			.apply {
				this.duration=duration
				this.interpolator = interpolator.interpolator
				addUpdateListener { animator -> update(animator) }
				start()
			}
	}
}