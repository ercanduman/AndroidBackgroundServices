package com.enbcreative.androidbackgroundservices.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.enbcreative.androidbackgroundservices.R
import com.enbcreative.androidbackgroundservices.ui.MainActivity

object Notifications {
    private const val CHANNEL_ID = "ServiceChannelExample"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create notification channel for only API >26.
            val notification = NotificationChannel(
                CHANNEL_ID,
                "Example Notification channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(notification)
        }
    }

    fun createNotification(context: Context, message: String): Notification {
        /**
         * Start activity if notification clicked.
         */
        val pendingIntent =
            PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), 0)


        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Service Content Title")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_baseline_notifications)
            .setContentIntent(pendingIntent)
            .build()
    }

}