package it.univpm.spottedkotlin.managers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.*
import com.google.gson.Gson
import it.univpm.spottedkotlin.extension.function.log
import it.univpm.spottedkotlin.model.Comment
import it.univpm.spottedkotlin.model.Post
import it.univpm.spottedkotlin.view.ViewPostActivity
import java.util.UUID
import java.util.concurrent.TimeUnit

object WorkerManager {
	val workersTag = "spotted"


	suspend fun fetchFollowedPosts(debug: Boolean = false): Data {
		val user = AccountManager.user
		val outputData = Data.Builder()
		val posts = mutableListOf<String>()

		for (postUID in user.following.sortedDescending().take(30)) {
			val post = DatabaseManager.get<Post>("posts/${postUID}") ?: continue
			val map = mapOf(
				"uid" to post.uid,
				"comments" to
						if (debug) null else
							post.comments.filter { it.authorUID != user.uid }
								.associateBy({ it.date.time }, { it.text }),
				"spotted" to if (debug) post.spotted else false
			)
			posts.add(Gson().toJson(map))
		}
		return outputData.putStringArray("posts", posts.toTypedArray()).build()
	}

	class NotificationWorker(val context: Context, params: WorkerParameters) :
		CoroutineWorker(context, params) {
		override suspend fun doWork(): Result {
			"start".log()

			val outputData = fetchFollowedPosts()
			val oldPosts =
				inputData.getStringArray("posts")?.map { Gson().fromJson(it, Map::class.java) }
			val newPosts =
				outputData.getStringArray("posts")?.map { Gson().fromJson(it, Map::class.java) }

			"input".log()
			oldPosts.toString().log()
			"output".log()
			newPosts.toString().log()

			if (oldPosts != null && newPosts != null)
				for (newPost in newPosts) {
					val oldPost = oldPosts.find { it["uid"] == newPost["uid"] } ?: continue
					var stop = false
					if (oldPost["spotted"] != newPost["spotted"]) {
						NotificationManager.notify(
							context,
							"Persona spottata!",
							"Un post che segui è stato spottato.",
							extras = listOf(
								Pair("goto", ViewPostActivity::class.java),
								Pair("postUID", oldPost["uid"] as String)
							)
						)
						stop = true
					}
					if (oldPost["comments"] != newPost["comments"]) {
						NotificationManager.notify(
							context,
							"Nuovi commenti!",
							"Un post che segui ha ricevuto nuovi commenti.",
							notificationChannel = NotificationManager.NotificationChannel.CHAT,
							extras = listOf(
								Pair("goto", ViewPostActivity::class.java),
								Pair("postUID", oldPost["uid"] as String),
								Pair("comments", true),
							)
						)
						stop = true
					}
					if (stop)
						break
				}

			return Result.success(outputData)
		}
	}

	suspend inline fun <reified T : CoroutineWorker> startPeriodicWorker(
		context: Context,
		id: String
	) {
		val constraints = Constraints.Builder()
			.setRequiredNetworkType(NetworkType.CONNECTED)
			.setRequiresBatteryNotLow(true)
//			.setRequiresDeviceIdle(true)
			.build()

		val workRequest =
			PeriodicWorkRequestBuilder<T>(
				120, TimeUnit.MINUTES, 60, TimeUnit.MINUTES
			)
				.setConstraints(constraints)
				.setInputData(
					fetchFollowedPosts()
				)
				.build()

		WorkManager.getInstance(context).enqueueUniquePeriodicWork(
			id, ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, workRequest
//			id, ExistingWorkPolicy.REPLACE, workRequest
		)
	}
}