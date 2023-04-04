package it.univpm.spottedkotlin.managers

import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.Tag

object DataManager {
	var posts:MutableList<Post>?=null
	var tags:MutableSet<Tag>?=null

	suspend fun fetchData(){
		posts= DatabaseManager.getList<Post>("posts", limit = 999)?.toMutableList()
//		tags= DatabaseManager.getList<Tag>("tags", limit = 999)?.toMutableSet()
	}
}