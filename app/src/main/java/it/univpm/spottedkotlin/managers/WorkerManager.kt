package it.univpm.spottedkotlin.managers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.*
import it.univpm.spottedkotlin.extension.function.log
import it.univpm.spottedkotlin.model.Comment
import it.univpm.spottedkotlin.model.Post
import java.util.UUID
import java.util.concurrent.TimeUnit

object WorkerManager {
	val workersTag = "spotted"

	class NotificationWorker(val context: Context, params: WorkerParameters) :
		CoroutineWorker(context, params) {
		override suspend fun doWork(): Result {

			NotificationManager.notify(
				context,
				"Novità sui post",
				"Ci sono delle novità sui tuoi post!"
			)

/*			DatabaseManager.observeNode<Post>(
				"posts",
				observer = { post ->
					if (post != null && AccountManager.user.following.contains(post.uid)) {
						NotificationManager.notify(
							context,
							"Novità sui post",
							if (post.authorUID == AccountManager.user.uid)
								"Ci sono delle novità sui tuoi post!"
							else
								"Ci sono delle novità sui post che segui!"

						)
					}
				}
			)*/
			return Result.success()
		}
	}

	inline fun <reified T : CoroutineWorker> startOneTimeWorker(context: Context) {
		val constraints = Constraints.Builder()
			.setRequiredNetworkType(NetworkType.CONNECTED)
			.setRequiresBatteryNotLow(true)
//			.setRequiresDeviceIdle(true)
			.build()
		val workRequest =
			PeriodicWorkRequestBuilder<T>(
				20, TimeUnit.SECONDS, 10, TimeUnit.SECONDS
			)
				.setConstraints(constraints)
				.build()

//		WorkManager.getInstance(context).getWorkInfoById(workersUUDI).get().state

		WorkManager.getInstance(context)
			.enqueue(workRequest)
	}
}