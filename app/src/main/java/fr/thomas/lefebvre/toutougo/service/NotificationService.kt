package fr.thomas.lefebvre.toutougo.service


import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log


class NotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        if(remoteMessage!!.notification!=null){
            Log.d("FCM",remoteMessage.data.toString())
        }

    }

    override fun onNewToken(s: String?) {
        Log.d("TOKEN_NEW",s!!)
        super.onNewToken(s)
    }




}
