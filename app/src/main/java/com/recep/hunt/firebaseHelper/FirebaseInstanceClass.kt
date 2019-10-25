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


/**
 * Created by Rishabh Shukla
 * on 2019-08-24
 * Email : rishabh1450@gmail.com
 */

class FirebaseInstanceClass : FirebaseMessagingService() {
    private lateinit var intent : Intent
    private lateinit var pendingIntent : PendingIntent

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        showLog(p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        val notification = p0.notification
        val channelId = "Default"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val CHANNEL_ID = "my_channel_01"
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(false)
            .setColor(Color.BLUE)
            .setContentTitle(p0.notification!!.title)
            .setSound(defaultSoundUri)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setChannelId(CHANNEL_ID)
            .setContentText(p0.notification!!.body).setAutoCancel(true).setContentIntent(pendingIntent)

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        // Sending Notification to OREO Version ---
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }

        manager.notify(0, builder.build())
    }

    private val TAG = FirebaseInstanceClass::class.java.simpleName
    private fun showLog(message:String){
        Log.e(TAG,message)
    }
}