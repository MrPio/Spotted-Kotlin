package it.univpm.spottedkotlin.managers

import it.univpm.spottedkotlin.extension.function.log
import it.univpm.spottedkotlin.model.Post
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DatabaseManagerTest {
	@Test
	fun testGetPaginatedList() {
		runBlocking {
			(1..3).forEach { _ ->
				DatabaseManager.getPaginatedList<Post>("posts").toString().log()
			}
		}
	}
}