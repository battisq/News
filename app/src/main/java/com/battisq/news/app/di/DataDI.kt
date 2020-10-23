package com.battisq.news.app.di

import androidx.room.Room
import com.battisq.news.data.api.NewsApi
import com.battisq.news.data.implementation.NewsApiRepositoryImpl
import com.battisq.news.data.implementation.NewsDatabaseRepositoryImpl
import com.battisq.news.data.room.AppDatabase
import com.battisq.news.domain.repositories.NewsApiRepository
import com.battisq.news.domain.repositories.NewsDatabaseRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    // Room
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "database")
            .build()
    }

    single { get<AppDatabase>().newsDao() }

    single { NewsDatabaseRepositoryImpl(get()) as NewsDatabaseRepository }

    // Retrofit
    single {
        Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single { get<Retrofit>().create(NewsApi::class.java) }

    single { NewsApiRepositoryImpl(get()) as NewsApiRepository }
}