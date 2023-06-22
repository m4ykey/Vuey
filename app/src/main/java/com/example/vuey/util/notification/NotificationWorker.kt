package com.example.vuey.util.notification

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.vuey.R
import java.util.Calendar

class NotificationWorker(
    context : Context,
    workerParams : WorkerParameters
) : Worker(context, workerParams) {
    override fun doWork(): Result {

        val CHANNEL_ID = "channelId"

        Notification(applicationContext).createNotificationChannel()

        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        if (currentHour < 15 || currentHour >= 16) {
            return Result.success()
        }

        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setContentTitle("Czego dzisiaj słuchałeś?")
            .setContentText("Zapisz swoje postępy w albumach")
            .setSmallIcon(R.drawable.logo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        val notificationManager = NotificationManagerCompat.from(applicationContext)

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return Result.success()
        }
        notificationManager.notify(1, notification)
        return Result.success()
    }
}