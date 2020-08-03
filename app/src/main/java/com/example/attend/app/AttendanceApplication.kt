package com.example.attend.app

import android.app.Application
import com.example.attend.app.di.databaseModule
import com.example.attend.app.di.repositoryModule
import com.example.attend.app.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class AttendanceApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@AttendanceApplication)
            modules(listOf(databaseModule, viewModelModule, repositoryModule))
        }
    }
}