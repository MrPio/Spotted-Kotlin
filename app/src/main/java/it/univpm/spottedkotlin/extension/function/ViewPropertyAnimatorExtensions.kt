package it.univpm.spottedkotlin.extension.function

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.ViewPropertyAnimator

fun ViewPropertyAnimator.onStart(listener: (animation: Animator) -> Unit): ViewPropertyAnimator {
	this.setListener(object : AnimatorListenerAdapter() {
		override fun onAnimationStart(animation: Animator) = listener(animation)
	})
	return this
}

fun ViewPropertyAnimator.onEnd(listener: (animation: Animator) -> Unit): ViewPropertyAnimator {
	this.setListener(object : AnimatorListenerAdapter() {
		override fun onAnimationEnd(animation: Animator) = listener(animation)
	})
	return this
}