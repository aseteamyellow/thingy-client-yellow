package ch.snipy.thingyClientYellow

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class DyrNotificationService : FirebaseMessagingService() {

    // For logging
    private val loggingTag = "DYR_NOTIFICATION_SERVICE"

    // to broadcast data to the main activity
    private lateinit var broadcaster: LocalBroadcastManager

    override fun onCreate() {
        super.onCreate()

        Log.d(loggingTag, "init firebase app")
        FirebaseApp.initializeApp(this)

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    Log.w(loggingTag, "getInstanceId failed", it.exception)
                }
                Log.d(loggingTag, "Token : ${it.result?.token}")
                onNewToken(it.result?.token)
            }

        Log.d(loggingTag, "Create the notification channel")
        createNotificationChannel()

        Log.d(loggingTag, "init the broadcaster")
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

    override fun onNewToken(token: String?) {
        Log.d(loggingTag, "Refreshed token :$token")
        val intent = Intent(getString(R.string.firebaseData))
        intent.putExtra("token", token)
        broadcaster.sendBroadcast(intent)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {

        Log.d(loggingTag, "Receive new message from ${remoteMessage?.from}")

        remoteMessage?.data?.isNotEmpty()?.let {
            Log.d(loggingTag, "Message data payload : " + remoteMessage.data)
        }

        // Check if message contains a notification payload.
        remoteMessage?.notification?.let {
            Log.d(loggingTag, "Message Notification Body: ${it.body}")
        }

        // Create the notification
        val notificationBuilder = NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id))
            .setSmallIcon(R.drawable.ic_priority_high_red_24dp)
            .setContentTitle("")
            .setContentText("")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationId = 1

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, notificationBuilder.build())
        }
    }

    private fun createNotificationChannel() {
        val name = getString(R.string.notification_channel_name)
        val descriptionText = getString(R.string.notification_channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel =
            NotificationChannel(
                getString(R.string.default_notification_channel_id),
                name,
                importance
            ).apply {
                description = descriptionText
            }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}
