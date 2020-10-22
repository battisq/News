package com.battisq.news.ui.list_news

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.battisq.news.data.api.NewsApi
import com.battisq.news.data.room.dao.NewsDao
import com.battisq.news.data.room.entities.NewsStory
import com.battisq.news.domain.network.NetworkState
import com.battisq.news.domain.network.Status
import com.battisq.news.ui.App
import com.battisq.news.ui.list_news.recycler.FetchDataWorker
import com.battisq.news.ui.list_news.recycler.NewsBoundaryCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get
import org.koin.core.parameter.parametersOf


class ListNewsViewModel(
    application: Application,
    private val newsDao: NewsDao,
    private val newsApi: NewsApi,
    pagingConfig: PagedList.Config? = null,
    private val fetchDataListener: FetchDataListener
) :
    AndroidViewModel(application),
    KoinComponent,
    LifecycleObserver {

    var recyclerViewState: Parcelable? = null

    private val boundaryCallback = get<NewsBoundaryCallback> {
        parametersOf(
            object : FetchDataWorker {
                override fun fetchData() {
                    this@ListNewsViewModel.fetchData()
                }
            },
            this::hasConnection,
            fetchDataListener::onFail,
            loadPageValue()
        )
    }

    private val newsListDataSource = newsDao.getAllNews().create()
    val newsList = newsDao.getAllNews()
        .toLiveData(
            config = pagingConfig!!,
            boundaryCallback = boundaryCallback
        )

    val networkState: MutableLiveData<NetworkState> = boundaryCallback.networkState

    fun refresh(onAction: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            boundaryCallback.page = 1
            newsDao.deleteAll()
            newsListDataSource.invalidate()
            viewModelScope.launch(Dispatchers.Main) { onAction() }
            retry(onAction)
        }
    }

    fun retry(onSuccess: () -> Unit) {
        boundaryCallback.page++
        fetchData()
        onSuccess()
    }

    private fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            networkState.postValue(NetworkState.LOADING)
            val list = mutableListOf<NewsStory>()
            val news = newsApi
                .getNews(page = boundaryCallback.page)
                .execute()
                .body()
                ?.articles

            news!!.forEach { value ->
                list.add(NewsStory.create(value))
            }

            newsDao.insertMany(list)
            networkState.postValue(NetworkState.LOADED)
        }
    }

    @SuppressLint("NewApi")
    fun hasConnection(): Boolean {
        val context: Context = getApplication()
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }

        return false
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
    fun onFail()
}