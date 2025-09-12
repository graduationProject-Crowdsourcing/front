package project.graduation.crowd_sourcing.presentation.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationServices
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await
import project.graduation.crowd_sourcing.domain.usecase.AlarmUseCase


@HiltWorker
class LocationWorker @AssistedInject  constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val alarmUseCase: AlarmUseCase
) : CoroutineWorker(appContext, workerParams) {

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(applicationContext)
    }

    private val context = appContext

    override suspend fun doWork(): Result {
        if (ActivityCompat.checkSelfPermission(
                applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            sendNotification("위치 권한 없음", "앱에 위치 권한을 허용해주세요.")
            return Result.failure()
        }

        return try {
            val location = fusedLocationClient.lastLocation.await()

            if (location != null) {
                val lat = location.latitude
                val lng = location.longitude

                // suspend 함수 직접 호출
                alarmUseCase.updateLocation(lat, lng).onFailure {
//                  sendNotification("updateLocation", it.toString())
                }.onSuccess {
//                    sendNotification("updateLocation", "성공")
                }

                Result.success()
            } else {
//                sendNotification("위치 확인 실패", "위치 정보를 가져올 수 없습니다.")
                Result.retry()
            }
        } catch (e: Exception) {
//            sendNotification("오류", "위치 정보를 가져오는 중 오류가 발생했습니다.")
            Result.retry()
        }
    }



    private fun sendNotification(title: String, content: String) {
        val channelId = "location_notify_channel"
        val channelName = "위치 알림 채널"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "1시간마다 위치 정보를 알림으로 표시"
            }
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_menu_mylocation)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(2001, notification)
    }
}
