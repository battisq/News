package com.battisq.news.ui.list_news.recycler

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList.BoundaryCallback
import com.battisq.news.R
import com.battisq.news.data.room.entities.NewsStory
import com.battisq.news.domain.network.NetworkState

class NewsBoundaryCallback(
    private val fetchDataWorker: FetchDataWorker,
    private val hasConnection: () -> Boolean,
    private val onFail: () -> Unit,
    initPageValue: Int
) :
    BoundaryCallback<NewsStory>() {

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()
    var page: Int = initPageValue
        set(value) {
            if (field in 1..5)
                field = value
        }

    override fun onItemAtEndLoaded(itemAtEnd: NewsStory) {
        if (hasConnection()) {
            if (page in 1..4) {
                page++
                Log.e(this::class.simpleName, page.toString())
                fetchDataWorker.fetchData()
            }
        } else {
            networkState.postValue(NetworkState.error("Отсутствует интернет"))
            onFail()
        }
    }

    override fun onZeroItemsLoaded() {
        if (hasConnection())
            fetchDataWorker.fetchData()
        else {
            NetworkState.error("Отсутствует интернет")
            onFail()
        }
    }
}

interface FetchDataWorker {
    fun fetchData()
}