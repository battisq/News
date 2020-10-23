package com.battisq.news

import com.battisq.news.app.di.*
import com.battisq.news.data.api.NewsApi
import com.battisq.news.domain.interactors.NewsDataInteractor
import org.junit.Before
import org.junit.Test
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.get
import org.junit.Assert.*

class RetrofitUnitTest : KoinComponent {

    val TAG: String? = RetrofitUnitTest::class.simpleName
    lateinit var interactor: NewsDataInteractor

    @Before
    fun init() {
        startKoin { modules(presentationModule, domainModule, dataModule) }
        interactor = get()
    }

    @Test
    fun getNews() {

    }
}