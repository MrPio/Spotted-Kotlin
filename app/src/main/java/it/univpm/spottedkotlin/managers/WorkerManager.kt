package it.univpm.spottedkotlin.managers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.*
import it.univpm.spottedkotlin.model.Comment
import it.univpm.spottedkotlin.model.Post

object WorkerManager {
	class NotificationWorker(val context: Context, params: WorkerParameters) :
		CoroutineWorker(context, params) {
		override suspend fun doWork(): Result {
//			NotificationManager.notify(context, "ciao", "Dal Worker")
			return Result.success()
		}
	}

	inline fun <reified T : CoroutineWorker> startOneTimeWorker(context: Context) {
		val constraints = Constraints.Builder()
			.setRequiredNetworkType(NetworkType.CONNECTED)
			.setRequiresBatteryNotLow(false)
			.build()
		val workRequest =
			OneTimeWorkRequestBuilder<T>()
				.setConstraints(constraints)
				.build()
		WorkManager.getInstance(context)
			.enqueue(workRequest)
	}
}