package it.univpm.spottedkotlin.managers

import it.univpm.spottedkotlin.extension.function.log


object BenchmarkManager {
	private var _lastTime = 0L

	fun lap(msg:String?=null) {
		if(msg!=null)
			log(msg)
		_lastTime = System.nanoTime()
	}

	private fun log(msg:String) = "[$msg] --> ${(System.nanoTime() - _lastTime) / 1000000.0}ms".log()
}