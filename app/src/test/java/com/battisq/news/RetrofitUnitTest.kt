package com.battisq.news

import com.battisq.news.data.api.NewsApi
import com.battisq.news.di.appModule
import com.battisq.news.di.retrofitModule
import com.battisq.news.di.roomDataSourceModule
import org.junit.Before
import org.junit.Test
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.get
import org.junit.Assert.*

class RetrofitUnitTest : KoinComponent {

    val TAG: String? = RetrofitUnitTest::class.simpleName
    lateinit var api: NewsApi

    @Before
    fun init() {
        startKoin { modules(appModule, retrofitModule, roomDataSourceModule) }
        api = get()
    }

    @Test
    fun getNews() {

        val news =  api
            .getNews(1)
            .execute()
            .body()
        assertEquals(news, news)
    }
}