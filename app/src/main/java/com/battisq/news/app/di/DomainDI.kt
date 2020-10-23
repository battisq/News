package com.battisq.news.app.di

import com.battisq.news.domain.interactors.NewsDataInteractor
import org.koin.dsl.module

val domainModule = module {
    single { NewsDataInteractor(get(), get()) }
}