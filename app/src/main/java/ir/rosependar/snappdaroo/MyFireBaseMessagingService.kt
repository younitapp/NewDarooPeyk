package ir.rosependar.snappdaroo


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val data = remoteMessage.data
        var myProductId: String?
        if (data["order_id"] != null) {
            myProductId = data["order_id"]
            sendNotification(
                remoteMessage.notification!!.title,
                remoteMessage.notification!!.body,
                "order_id",
                myProductId
            )
        }
        if (data["category_id"] != null) {
            myProductId = data["category_id"]
            sendNotification(
                remoteMessage.notification!!.title,
                remoteMessage.notification!!.body,
                "category_id",
                myProductId
            )
        }
        if (data["internal_url"] != null) {
            myProductId = data["internal_url"]
            sendNotification(
                remoteMessage.notification!!.title,
                remoteMessage.notification!!.body,
                "internal_url",
                myProductId
            )
        }
        if (data["dialog_link"] != null) {
            myProductId = data["dialog_link"]
            sendNotification(
                remoteMessage.notification!!.title,
                remoteMessage.notification!!.body,
                "dialog_link",
                myProductId
            )
        }
    }

    private fun sendNotification(
        title: String?,
        messageBody: String?,
        tag: String,
        link: String?
    ) {
        /*val intent = Intent(this, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        if (link != null) {
            intent.putExtra(tag, link)
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )*/
        val defaultSoundUri =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder =
            NotificationCompat.Builder(this)
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
        notificationBuilder.setContentTitle(title)
        notificationBuilder.setContentText(messageBody)
        notificationBuilder.setAutoCancel(true)
        notificationBuilder.setSound(defaultSoundUri)
        /*notificationBuilder.setContentIntent(pendingIntent)*/
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }
}
