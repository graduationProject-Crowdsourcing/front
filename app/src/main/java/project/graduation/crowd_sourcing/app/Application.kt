package project.graduation.crowd_sourcing.app

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HiltApplication: Application() {
    companion object {
        lateinit var instance: HiltApplication
            private set

        val context: Context
            get() = instance.applicationContext
    }
    override fun onCreate() {
        super.onCreate()
        instance = this

    }
}