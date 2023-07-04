package it.univpm.spottedkotlin.managers

import android.Manifest
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import it.univpm.spottedkotlin.R
import it.univpm.spottedkotlin.enums.Colors
import it.univpm.spottedkotlin.view.FirstActivity
import it.univpm.spottedkotlin.view.MainActivity
import it.univpm.spottedkotlin.view.ViewPostActivity

object NotificationManager {
	enum class NotificationChannel(
		val id: String,
		val title: String,
		val description: String,
		val importance: Int = android.app.NotificationManager.IMPORTANCE_DEFAULT
	) {
		GENERAL("0", "Persone spottate", "Notifiche di post seguiti spottati"),
		CHAT("1", "Nuovi commenti", "Notifiche dei commenti ai post seguiti"),
	}

	private const val CHANNEL_ID = "spotted"
	private const val NOTIFICATION_ID = 0

	private fun getNotificationManager(context: Context) =
		context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager


	private fun createNotificationChannel(
		context: Context,
		notificationChannel: NotificationChannel,
	) {
		if (getNotificationManager(context).getNotificationChannel(notificationChannel.id) == null) {
			val channel = NotificationChannel(
				notificationChannel.id,
				notificationChannel.title,
				notificationChannel.importance
			).apply { description = notificationChannel.description }
			getNotificationManager(context).createNotificationChannel(channel)
		}
	}

	fun notify(
		context: Context,
		title: String,
		message: String,
		secondTitle: String? = null,
		secondMessage: String? = null,
		secondSubtitle: String? = null,
		notificationChannel: NotificationChannel = NotificationChannel.GENERAL,
		extras: List<Pair<String, java.io.Serializable>> = listOf()
	) {
		createNotificationChannel(context, notificationChannel)

		val intent = Intent(context, FirstActivity::class.java).apply {
			extras.forEach {
				putExtra(
					it.first,
					it.second
				)
			}
		}
		val pendingIntent =
			PendingIntent.getActivity(
				context,
				0,
				intent,
				PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
			)

		val bigText = NotificationCompat.BigTextStyle()
		bigText.bigText(secondSubtitle)
		bigText.setBigContentTitle(secondMessage)
		bigText.setSummaryText(secondTitle)
		val notification = NotificationCompat.Builder(context, CHANNEL_ID)
			.setSmallIcon(R.drawable.notifiction_small_icon)
			.setContentTitle(title)
			.setContentText(message)
			.setChannelId(notificationChannel.id)
			.setPriority(NotificationCompat.PRIORITY_DEFAULT)
			.setStyle(bigText)
			.setContentIntent(pendingIntent)
			.setAutoCancel(true)
			.build()

		// Display the notification
		val notificationManager = NotificationManagerCompat.from(context)
		if (ActivityCompat.checkSelfPermission(
				context, Manifest.permission.POST_NOTIFICATIONS
			) == PackageManager.PERMISSION_GRANTED
		) {
			notificationManager.notify(notificationChannel.id.toInt(), notification)
		}
	}
}