package com.battisq.news.presentation.ui.list_news.recycler

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList.BoundaryCallback
import com.battisq.news.domain.entities.NewsStory
import com.battisq.news.domain.network.NetworkState

class NewsBoundaryCallback(
    private val fetchDataWorker: FetchDataWorker,
    private val hasConnection: () -> Boolean,
    initPageValue: Int
) : BoundaryCallback<NewsStory>() {

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()
    var page: Int = initPageValue
        set(value) {
            if (field in 1..5)
                field = value
        }

    override fun onItemAtEndLoaded(itemAtEnd: NewsStory) {
        if (hasConnection()) {
            when {
                page in 1..4 -> {
                    page++
                    Log.e(this::class.simpleName, page.toString())
                    fetchDataWorker.fetchData()
                }
                page > 4 -> fetchDataWorker.onEndData()
            }
        } else {
            noConnectionFail()
        }
    }

    override fun onZeroItemsLoaded() {
        if (hasConnection())
            fetchDataWorker.fetchData()
        else {
            noConnectionFail()
        }
    }

    private fun noConnectionFail() {
        "Отсутствует интернет".let {
            networkState.postValue(NetworkState.error(it))
            fetchDataWorker.onFailData(it)
        }
    }
}

interface FetchDataWorker {
    fun fetchData()
    fun onFailData(message: String?)
    fun onEndData()
}