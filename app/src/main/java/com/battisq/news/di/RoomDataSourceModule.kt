package com.battisq.news.di

import androidx.room.Room
import com.battisq.news.data.room.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val roomDataSourceModule = module {

    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "database")
            .build()
    }

    single { get<AppDatabase>().newsDao() }
}