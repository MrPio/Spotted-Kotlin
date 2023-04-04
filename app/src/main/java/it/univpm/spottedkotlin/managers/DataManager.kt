package it.univpm.spottedkotlin.managers

import android.content.Context
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.model.Tag

object DataManager {
	var posts: List<Post>? = null
	var tags: Set<Tag>?=null

	suspend fun fetchData(context: Context) {
		posts = DatabaseManager.getList<Post>("posts", limit = 999)?.toList()
//		tags= DatabaseManager.getList<Tag>("tags", limit = 999)?.toSet()
		tags = DummyManager.generateTags(context)
	}
}