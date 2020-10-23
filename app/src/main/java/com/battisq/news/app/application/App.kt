package com.battisq.news.app.application

import android.app.Application
import com.battisq.news.app.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)

            modules(
                listOf(
                    presentationModule,
                    domainModule,
                    dataModule
                )
            )
        }
    }
}