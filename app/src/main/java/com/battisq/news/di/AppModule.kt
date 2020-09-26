package com.battisq.news.di

import androidx.paging.PagedList
import com.battisq.news.ui.list_news.ListNewsViewModel
import com.battisq.news.ui.list_news.recycler.NewsBoundaryCallback
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel {
        (config: PagedList.Config) ->
        ListNewsViewModel(get(), config)
    }

    factory {
        NewsBoundaryCallback(get(), get())
    }
}