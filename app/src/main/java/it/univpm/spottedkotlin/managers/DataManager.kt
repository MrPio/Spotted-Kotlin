package it.univpm.spottedkotlin.managers

import it.univpm.spottedkotlin.model.Post

object DataManager {
	var posts:MutableList<Post>?=null

	suspend fun fetchData(){
		posts= DatabaseManager.getList<Post>("posts", limit = 999)?.toMutableList()
	}
}