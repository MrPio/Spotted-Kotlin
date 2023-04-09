package it.univpm.spottedkotlin.managers

import android.animation.Animator
import android.animation.ValueAnimator
import it.univpm.spottedkotlin.enums.TimesInterpolator

object AnimationManager {
	fun animate(
		start: Number,
		end: Number,
		interpolator: TimesInterpolator = TimesInterpolator.LINEAR,
		duration: Long = 250L,
		update: (value: Number) -> Unit,
		endListener: ((animator: Animator) -> Unit)?=null
	) {
		ValueAnimator.ofFloat(start.toFloat(), end.toFloat())
			.apply {
				this.duration = duration
				this.interpolator = interpolator.interpolator
				addUpdateListener { animator -> update(animator.animatedValue as Number) }
				if (endListener != null) {
					addListener(object : Animator.AnimatorListener {
						override fun onAnimationStart(animation: Animator) {}
						override fun onAnimationCancel(animation: Animator) {}
						override fun onAnimationRepeat(animation: Animator) {}
						override fun onAnimationEnd(animation: Animator) =
							endListener(animation)
					})
				}
				start()
			}
	}
}