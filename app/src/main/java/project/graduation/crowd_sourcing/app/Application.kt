package project.graduation.crowd_sourcing.app

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.presentation.utils.LocationWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class HiltApplication : Application(), Configuration.Provider {

    companion object {
        lateinit var instance: HiltApplication
            private set

        val context: Context
            get() = instance.applicationContext
    }

    @Inject lateinit var workerFactory: HiltWorkerFactory



    override fun onCreate() {
        super.onCreate()
        instance = this
        CoroutineScope(Dispatchers.Default).launch {
            delay(100)
            registerPeriodicLocationWorker()
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private fun registerPeriodicLocationWorker() {
        val workerManager = WorkManager.getInstance(this)
        workerManager.cancelAllWork()

        val request = PeriodicWorkRequestBuilder<LocationWorker>(15, TimeUnit.MINUTES)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()


        workerManager.enqueueUniquePeriodicWork(
            "LocationNotifyWork",
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            request
        )

        val request2 = androidx.work.OneTimeWorkRequestBuilder<LocationWorker>()
            .setInitialDelay(5, TimeUnit.SECONDS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        WorkManager.getInstance(this).enqueueUniqueWork(
            "LocationNotifyWorkTest",
            ExistingWorkPolicy.REPLACE,
            request2
        )
    }
}
