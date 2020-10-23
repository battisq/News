package com.battisq.news.presentation.ui.list_news

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Message
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.DataSource
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.battisq.news.domain.entities.NewsStory
import com.battisq.news.domain.network.NetworkState
import com.battisq.news.app.application.App
import com.battisq.news.domain.interactors.NewsDataInteractor
import com.battisq.news.presentation.repositories.WirelessServicesRepository
import com.battisq.news.presentation.ui.list_news.recycler.FetchDataWorker
import com.battisq.news.presentation.ui.list_news.recycler.NewsBoundaryCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent


class ListNewsViewModel(
    application: Application,
    pagingConfig: PagedList.Config? = null,
    private val fetchDataListener: FetchDataListener,
    private val wirelessServicesRepository: WirelessServicesRepository,
    private val newsDataInteractor: NewsDataInteractor
) :
    AndroidViewModel(application),
    KoinComponent,
    LifecycleObserver {

    var recyclerViewState: Parcelable? = null
    var networkState: MutableLiveData<NetworkState>
    lateinit var newsList: LiveData<PagedList<NewsStory>>
    private lateinit var newsListDataSource: DataSource<Int, NewsStory>
    private var boundaryCallback: NewsBoundaryCallback

    init {
        boundaryCallback = NewsBoundaryCallback(
            object : FetchDataWorker {
                override fun fetchData() {
                    this@ListNewsViewModel.fetchNews()
                }

                override fun onFailData(message: String?) = fetchDataListener.onFail(message)
                override fun onEndData() = fetchDataListener.onEndData()
            },
            this::hasConnection,
            loadPageValue()
        )

        viewModelScope.launch(Dispatchers.IO) {
            newsListDataSource = newsDataInteractor.getAllNews().create()
            newsList = newsDataInteractor.getAllNews().toLiveData(
                config = pagingConfig!!,
                boundaryCallback = boundaryCallback
            )

        }
        networkState = boundaryCallback.networkState
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            boundaryCallback.page = 1
            newsDataInteractor.deleteAllNews()
            newsListDataSource.invalidate()
            retry({})
        }
    }

    fun retry(onSuccess: () -> Unit) {
        boundaryCallback.page++
        fetchNews()
        onSuccess()
    }

    private fun fetchNews() {
        viewModelScope.launch(Dispatchers.IO) {
            networkState.postValue(NetworkState.LOADING)
            newsDataInteractor.fetchNews(boundaryCallback.page)
            networkState.postValue(NetworkState.LOADED)
        }
    }

    fun hasConnection(): Boolean {
        return wirelessServicesRepository.hasInternetConnection(getApplication())
    }

    private fun loadPageValue(): Int {
        val pref = getApplication<App>().getSharedPreferences("news_pref", MODE_PRIVATE)
        val value = pref.getInt("page_number", 1)
        Log.e(this::class.simpleName, value.toString())
        return value
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun savePageValue() {
        val sPref = getApplication<App>().getSharedPreferences("news_pref", MODE_PRIVATE)
        val ed: SharedPreferences.Editor = sPref.edit()
        ed.putInt("page_number", boundaryCallback.page)
        ed.apply()

        Log.e(this::class.simpleName + "event_on_pause", boundaryCallback.page.toString())
    }
}

interface FetchDataListener {
    fun onSuccess()
    fun onFail(message: String?)
    fun onEndData()
}