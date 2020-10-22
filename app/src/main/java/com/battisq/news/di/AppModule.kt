package com.battisq.news.di

import androidx.paging.PagedList
import com.battisq.news.ui.list_news.FetchDataListener
import com.battisq.news.ui.list_news.ListNewsViewModel
import com.battisq.news.ui.list_news.recycler.FetchDataWorker
import com.battisq.news.ui.list_news.recycler.ListNewsAdapter
import com.battisq.news.ui.list_news.recycler.NewsBoundaryCallback
import com.battisq.news.ui.map.MapViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { (config: PagedList.Config,
                    listener: FetchDataListener) ->
        ListNewsViewModel(get(), get(), get(), config, listener)
    }

    viewModel {
        MapViewModel(get())
    }

    single { (worker: FetchDataWorker,
                 hasConnection: () -> Boolean,
                 onFail: () -> Unit,
                 page: Int) ->
        NewsBoundaryCallback(worker, hasConnection, onFail, page)
    }

    factory { (retryCallback: () -> Unit) -> ListNewsAdapter(retryCallback) }
}