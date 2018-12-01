package ch.snipy.thingyClientYellow

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class DyrNotificationService : FirebaseMessagingService() {

    private val loggingTag = "DYR_NOTIFICATION_SERVICE"

    override fun onCreate() {
        super.onCreate()

        Log.d(loggingTag, "init firebase app")
        FirebaseApp.initializeApp(this)

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    Log.w(loggingTag, "getInstaceId failed", it.exception)
                }
                Log.d(loggingTag, "Token : ${it.result?.token}")
            }

        Log.d(loggingTag, "Create the notification channel")
        createNotificationChannel()
    }

    override fun onNewToken(token: String?) {
        Log.d(loggingTag, "Refreshed token :$token")
        sendRegistrationToServer(token)
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
            notify(notificationId,notificationBuilder.build())
        }
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
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
