package com.jj.revera

import android.app.Application
import com.jj.revera.shared.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class ReveraApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@ReveraApplication)
        }
    }
}
