package com.example.vuey.util.notification

import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.vuey.util.Constants.CHANNEL_ID
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i("FCM_TOKEN", token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        message.notification?.let {
            val title = it.title
            val body = it.body

            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText(body)
                .setContentTitle(title)
                .build()

            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(1002, notification)
        }

        Log.i("DATA_MESSAGE", message.data.toString())
    }

}