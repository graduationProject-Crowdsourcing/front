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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.domain.model.Noti
import project.graduation.crowd_sourcing.presentation.di.FcmUseCaseEntryPoint
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

        val workId = remoteMessage.data["workId"]?.toIntOrNull() ?: -1
        val memberId = remoteMessage.data["memberId"]?.toIntOrNull() ?: -1

        val acceptIntent = Intent(this, NotificationActionReceiver::class.java).apply {
            action = "ACTION_ACCEPT"
            putExtra("EXTRA_WORK_ID", workId)
            putExtra("EXTRA_MEMBER_ID", memberId)
        }
        val rejectIntent = Intent(this, NotificationActionReceiver::class.java).apply {
            action = "ACTION_REJECT"
            putExtra("EXTRA_WORK_ID", workId)
            putExtra("EXTRA_MEMBER_ID", memberId)
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
            .setPriority(NotificationCompat.PRIORITY_HIGH).let{
                if(!title.contains("승인")){
                    it.addAction(android.R.drawable.ic_menu_send, "수락", acceptPendingIntent)
                        .addAction(android.R.drawable.ic_menu_close_clear_cancel, "거절", rejectPendingIntent).build()
                } else{
                    it.build()
                }
            }



        notificationManager.notify(101, notification)

        CoroutineScope(Dispatchers.IO).launch {
            notiUseCase.insertNote(Noti(title = title, content = body))
        }
    }

}


class NotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val entryPoint = EntryPointAccessors.fromApplication(
            context.applicationContext,
            FcmUseCaseEntryPoint::class.java
        )
        val postAcceptUseCase = entryPoint.postAcceptUseCase()
        val postRejectWorkUseCase = entryPoint.postRejectWorkUseCase()

        // 알림에서 전달된 값 꺼내기
        val workId = intent.getIntExtra("EXTRA_WORK_ID", -1)
        val memberId = intent.getIntExtra("EXTRA_MEMBER_ID", -1)

        when (intent.action) {
            "ACTION_ACCEPT" -> {
                Log.d("FCM_ACTION", "수락 누름: workId=$workId, memberId=$memberId")

                CoroutineScope(Dispatchers.IO).launch {
                    val result = postAcceptUseCase(workId, memberId)
                    if (result.isSuccess) {
                        Log.d("FCM_ACTION", "수락 API 성공")

                        // 홈(MainActivity) 이동
                        val launchIntent = Intent(Intent.ACTION_MAIN).apply {
                            addCategory(Intent.CATEGORY_LAUNCHER)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            setClassName(
                                context.packageName,
                                "project.graduation.crowd_sourcing.app.MainActivity"
                            )
                        }
                        context.startActivity(launchIntent)

                        // 알림 제거
                        val notificationManager =
                            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.cancel(101)
                    } else {
                        Log.e("FCM_ACTION", "수락 API 실패")
                    }
                }
            }

            "ACTION_REJECT" -> {
                Log.d("FCM_ACTION", "거절 누름: workId=$workId, memberId=$memberId")

                CoroutineScope(Dispatchers.IO).launch {
                    val result = postRejectWorkUseCase(workId, memberId)
                    if (result.isSuccess) {
                        Log.d("FCM_ACTION", "거절 API 성공")

                        val notificationManager =
                            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.cancel(101)
                    } else {
                        Log.e("FCM_ACTION", "거절 API 실패")
                    }
                }
            }
        }
    }
}
