package it.univpm.spottedkotlin.managers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters

object WorkerManager {

	fun startNotificationWorker(context: Context) {
		class Worker(context: Context, params: WorkerParameters) :
			CoroutineWorker(context, params) {
			override suspend fun doWork(): Result {
//				NotificationManager.notify(context, "ciao", "descrdescrdescrdescrdescr")
				return Result.success()
			}
		}

		val uploadWorkRequest =
			OneTimeWorkRequestBuilder<Worker>().build()
		WorkManager.getInstance(context)
			.enqueue(uploadWorkRequest)
	}
}