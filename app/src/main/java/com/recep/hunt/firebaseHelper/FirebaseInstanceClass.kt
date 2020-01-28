package com.recep.hunt.firebaseHelper

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.recep.hunt.R
import com.recep.hunt.profile.UserProfileSettingsActivity
import kotlin.random.Random


/**
 * Created by Rishabh Shukla
 * on 2019-08-24
 * Email : rishabh1450@gmail.com
 */

class FirebaseInstanceClass : FirebaseMessagingService() {
    private lateinit var intent : Intent
    private lateinit var pendingIntent : PendingIntent

    val NOTIFICATION_CHANNEL_NAME="hunt_fcm_channel"
    val NOTIFICATION_CHANNEL_ID = "com.recep.hunt.notification"


    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        showLog(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        Log.i("onMessageReceived","onMessageReceived${p0.notification.toString()}")
        val intent = Intent(this,UserProfileSettingsActivity::class.java)
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val notification = p0.notification
        createAlertNotification(Random.nextInt(),notification?.title?:"",notification?.body?:"",pendingIntent,this)
    }

    private val TAG = FirebaseInstanceClass::class.java.simpleName
    private fun showLog(message:String){
        Log.e(TAG,message)
    }



    fun createAlertNotification(id: Int, title: String, text: String, pendingIntent: PendingIntent? = null,context:Context) {

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val inboxStyle = NotificationCompat.InboxStyle()
        inboxStyle.setBigContentTitle(title)
        inboxStyle.addLine(text)

        val notificationManager = context. getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setAutoCancel(true)
            .setColor(context.resources.getColor(R.color.colorAccent))
            .setSound(defaultSoundUri)
            .setContentText(text)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setStyle(inboxStyle)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH)
                .let { notificationManager.createNotificationChannel(it) }

        } else {
            notificationBuilder.priority = NotificationCompat.PRIORITY_HIGH
        }



        notificationManager.notify(id, notificationBuilder.build())
    }
}