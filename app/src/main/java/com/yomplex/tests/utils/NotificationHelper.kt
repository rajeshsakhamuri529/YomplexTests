package com.yomplex.tests.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import android.util.Log


import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.yomplex.tests.R
import com.yomplex.tests.activity.GradeActivity
import java.util.*

class NotificationHelper : FirebaseMessagingService() {

    val TAG = "Service"
    var sharedPrefs: SharedPrefs? = null
    var notification: Boolean = true
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.i(TAG, token)
        Log.e("NotificationHelper","new token....token...."+token)
        sharedPrefs?.setPrefVal(applicationContext, "firebasetoken", token)
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e("notification helper","on message received......");
        sharedPrefs = SharedPrefs()
        Log.e(TAG, "From: " + remoteMessage!!.from)
        Log.e(TAG, "From: " + remoteMessage!!.data)
        /*if(remoteMessage.data != null){
            sharedPrefs!!.setPrefVal(applicationContext,"screen",
                remoteMessage.data.get("screen")!!
            )
        }*/

       // Log.e(TAG, "Notification Message Body: " + remoteMessage.data.get("my_custom_key2"))
        notification = sharedPrefs?.getBooleanPrefVal(applicationContext, ConstantPath.NOTIFICATION) ?: true
        //if(notification){
            sendNotification(remoteMessage)
       // }

        /*val intent = Intent(this@NotificationHelper, GradeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("message", remoteMessage.notification!!.body!!)
        startActivity(intent)*/
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        val intent = Intent(this, GradeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
       // val NOTIFICATION_CHANNEL_ID = "com.blobcity.utils"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID = "com.blobcity.utils"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", notificationManager.)
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_DEFAULT)

            notificationChannel.description = "YOYO"
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE

            notificationManager.createNotificationChannel(notificationChannel)
        }

        val notifbuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        val vibrate = longArrayOf(0, 100, 200, 300)
        notifbuilder.setAutoCancel(true)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_stat_default)
            .setContentTitle(remoteMessage.notification!!.title)
            .setContentText(remoteMessage.notification!!.body)
            .setContentInfo("Info")
            .setColor(Color.BLACK)
            .setSound(defaultSoundUri)
            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_default))
            .setVibrate(vibrate)
            .setContentIntent(pendingIntent)

        notifbuilder.setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_stat_default))
        notifbuilder.color = resources.getColor(R.color.black)


        notificationManager.notify(Random().nextInt(), notifbuilder.build())
        //Log.d("sendNotification",remoteMessage.data.toString()+"!")
       /* val notificationBuilder = NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID)
            .setContentText(remoteMessage.notification?.body)
            .setContentTitle(remoteMessage.notification?.title)
            .setAutoCancel(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1 *//* ID of notification*//* , notificationBuilder.build())*/
    }
}