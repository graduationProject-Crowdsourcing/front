package project.graduation.crowd_sourcing.presentation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.migration.CustomInjection.inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.domain.model.Noti
import project.graduation.crowd_sourcing.domain.usecase.NotiUseCase
import project.graduation.crowd_sourcing.presentation.di.NotiUseCaseEntryPoint

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "FCM 토큰: $token")

    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            NotiUseCaseEntryPoint::class.java
        )

        val notiUseCase = entryPoint.notiUseCase()

        val title = remoteMessage.notification?.title ?: "알림"
        val body = remoteMessage.notification?.body ?: "내용 없음"

        Log.d("FCM", "푸시 수신: $title - $body")

        val channelId = "default_channel"

        val acceptIntent = Intent(this, NotificationActionReceiver::class.java).apply {
            action = "ACTION_ACCEPT"
        }
        val rejectIntent = Intent(this, NotificationActionReceiver::class.java).apply {
            action = "ACTION_REJECT"
        }

        val acceptPendingIntent = PendingIntent.getBroadcast(
            this, 0, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val rejectPendingIntent = PendingIntent.getBroadcast(
            this, 1, rejectIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "기본 채널", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_history_work)

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_history_work) // 알림 아이콘
            .setLargeIcon(largeIcon)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(android.R.drawable.ic_menu_send, "수락", acceptPendingIntent)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "거절", rejectPendingIntent)
            .build()

        notificationManager.notify(0, notification)

        CoroutineScope(Dispatchers.IO).launch {
            notiUseCase.insertNote(Noti(title = title, content = body))
        }
    }

}


class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            "ACTION_ACCEPT" -> {
                Log.d("FCM_ACTION", "수락 누름")
            }

            "ACTION_REJECT" -> {
                Log.d("FCM_ACTION", "거절 누름")
            }
        }
    }
}
