package it.univpm.spottedkotlin.managers

import android.util.Log

object LogManager {
	val TAG="MrPio"
	fun log(msg:String)= Log.d(TAG,msg)
}