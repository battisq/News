package com.battisq.news.app.di

import androidx.paging.Config
import com.battisq.news.presentation.repositories.WirelessServicesRepository
import com.battisq.news.presentation.repositories.WirelessServicesRepositoryImpl
import com.battisq.news.presentation.ui.list_news.ListNewsViewModel
import com.battisq.news.presentation.ui.map.MapViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel {
        ListNewsViewModel(get(), get(), get(), get())
    }

    viewModel {
        MapViewModel(get(), get())
    }

    factory { WirelessServicesRepositoryImpl() as WirelessServicesRepository }
    single {
        Config(
            pageSize = 5,
            prefetchDistance = 5,
            enablePlaceholders = true
        )
    }
}
