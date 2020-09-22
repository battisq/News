package com.battisq.news.di

import androidx.paging.PagedList
import com.battisq.news.ui.list_news.ListNewsViewModel
import com.battisq.news.ui.list_news.recycler.NewsBoundaryCallback
import com.battisq.news.ui.list_news.recycler.NewsPositionalDataSource
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
//        ListNewsViewModel(get())
        (config: PagedList.Config) ->
        ListNewsViewModel(get(), config)
    }

    factory {
        NewsPositionalDataSource(get())
    }

    single {
        NewsBoundaryCallback(get(), get())
    }
}