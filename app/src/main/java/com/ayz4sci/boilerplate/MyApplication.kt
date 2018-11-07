package com.ayz4sci.boilerplate

import android.app.Application
import com.ayz4sci.boilerplate.di.appModule
import com.ayz4sci.boilerplate.di.viewModelModule
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import org.koin.android.ext.android.startKoin

/**
 * Created by @ayz4sci on 27/06/2018.
 */

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.ENABLE_CRASHLYTICS)
            Fabric.with(this, Crashlytics())

        startKoin(this, listOf(appModule, viewModelModule))
    }
}