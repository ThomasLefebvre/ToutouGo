package fr.thomas.lefebvre.toutougo.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.Notification
import android.app.NotificationManager
import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.Color
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import androidx.core.app.NotificationCompat
import fr.thomas.lefebvre.toutougo.R
import java.util.*


class NotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        shownotification(remoteMessage!!.notification!!)
    }

    override fun onNewToken(s: String?) {
        Log.d("TOKEN_NEW",s!!)
        super.onNewToken(s)
    }


    fun shownotification(message: RemoteMessage.Notification) {


        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID = "fr.thomas.lefebvre.toutougo"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID, "Notification",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationChannel.description = "ToutouGo"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)

        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_dog)
            .setContentTitle(message.title)
            .setContentText(message.body)
            .setContentInfo("Info")

        notificationManager.notify(Random().nextInt(), notificationBuilder.build())

    }


}
