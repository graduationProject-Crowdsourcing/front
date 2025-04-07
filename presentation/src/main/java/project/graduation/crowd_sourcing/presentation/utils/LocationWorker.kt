package project.graduation.crowd_sourcing.presentation.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

@HiltWorker
class LocationWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {
    @Inject
    lateinit var fusedLocationClient: FusedLocationProviderClient

    val context = appContext
    // 이전 위치는 SharedPreferences 등에 저장해두는 방식
    private val prefs = appContext.getSharedPreferences("location_prefs", Context.MODE_PRIVATE)

    override suspend fun doWork(): Result {
        return suspendCancellableCoroutine { cont ->
            if (ActivityCompat.checkSelfPermission(
                    applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                cont.resume(Result.failure(), null)
                return@suspendCancellableCoroutine
            }

            fusedLocationClient.lastLocation.addOnSuccessListener { newLocation ->
                if (newLocation != null) {
                    val lastLat = prefs.getFloat("last_lat", 0f).toDouble()
                    val lastLng = prefs.getFloat("last_lng", 0f).toDouble()

                    val result = FloatArray(1)
                    Location.distanceBetween(
                        lastLat, lastLng,
                        newLocation.latitude, newLocation.longitude,
                        result
                    )

                    val distance = result[0]

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val name = "거리 알림"
                        val descriptionText = "사용자의 위치 이동 거리 알림"
                        val importance = NotificationManager.IMPORTANCE_DEFAULT
                        val channel = NotificationChannel("distance_channel", name, importance).apply {
                            description = descriptionText
                        }
                        val notificationManager: NotificationManager =
                            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.createNotificationChannel(channel)
                    }

                    if (distance >= 250f) {
                        // ✅ TODO: API 호출 로직
                        Log.d("LocationWorker", "Moved over 250m, distance=$distance")

                        // 이전 위치 업데이트
                        prefs.edit()
                            .putFloat("last_lat", newLocation.latitude.toFloat())
                            .putFloat("last_lng", newLocation.longitude.toFloat())
                            .apply()
                    } else {
                        Log.d("LocationWorker", "Not moved enough: $distance")
                    }

                    cont.resume(Result.success(), null)
                } else {
                    cont.resume(Result.retry(), null)
                }
            }.addOnFailureListener {
                cont.resume(Result.retry(), null)
            }
        }
    }
}
