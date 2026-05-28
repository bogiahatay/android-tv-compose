package net.habui.tv

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class TvApplication : Application() {
    override fun onCreate() {
        super.onCreate()


        Timber.plant(
            Timber.DebugTree()
        )
    }
}
