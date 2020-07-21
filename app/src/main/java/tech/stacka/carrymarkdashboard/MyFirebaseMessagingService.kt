package tech.stacka.carrymarkdashboard

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import tech.stacka.carrymarkdashboard.activity.home.HomeActivity
import tech.stacka.carrymarkdashboard.models.Config

class MyFirebaseMessagingService : FirebaseMessagingService() {
    var push_title: String? = null
    var push_message: String? = null
    var intent: Intent? = null
    private lateinit var sharedPreference: SharedPreferences
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // ...
        val intent = Intent(applicationContext, HomeActivity::class.java)
        intent.putExtra("from", "notification")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

        //  Handle FCM messages here.
        Log.d(
            TAG,
            "From: " + remoteMessage.from
        )

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(
                TAG,
                "Message data payload: " + remoteMessage.data
            )
            val data: Map<String, String> = remoteMessage.data
            Config.title = data["title"].toString()
            Config.content = data["message"].toString()
            sharedPreference=PreferenceManager.getDefaultSharedPreferences(this@MyFirebaseMessagingService)
            val editor = sharedPreference.edit()
            editor.putString("pushTitle", Config.title)
            editor.putString("pushBody", Config.content)
            editor.apply()
            sendNotification(Config.title, Config.content, pendingIntent)
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(
                TAG, "Message Notification Body: " + remoteMessage.notification!!.body
            )
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        val editor = sharedPreference.edit()
        editor.putString("fireBaseToken", token)
        editor.apply()
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }

    private fun sendNotification(title: String, message: String, resultPendingIntent: PendingIntent) {

//        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
//        style.bigPicture(bitmap);
        val defaultSound =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        // Uri soundUri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.church_bell_sound);
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANNEL_ID = "101"
        val iconLarge = BitmapFactory.decodeResource(
            applicationContext.resources,
            R.mipmap.ic_launcher1_round
        )
        val notificationBuilder =
            NotificationCompat.Builder(
                applicationContext,
                NOTIFICATION_CHANNEL_ID
            )
                .setSmallIcon(R.mipmap.ic_launcher1_round)
                .setContentTitle(title)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentText(message)
                .setContentIntent(resultPendingIntent) //                .setStyle(style)
                .setLargeIcon(iconLarge)
                .setWhen(System.currentTimeMillis())
                .setPriority(Notification.PRIORITY_MAX)
                .setChannelId(NOTIFICATION_CHANNEL_ID)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (defaultSound != null) {
                // Changing Default mode of notification
                notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE)
                // Creating an Audio Attribute
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()

                // Creating Channel
                val notificationChannel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "church_bell_sound",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationChannel.setSound(defaultSound, audioAttributes)
                notificationManager.createNotificationChannel(notificationChannel)
            }
        }
        notificationManager.notify(1, notificationBuilder.build())
    }

    companion object {
        const val TAG = "FCM"
    }
}