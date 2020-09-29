package com.battisq.news.ui.list_news

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.battisq.news.data.api.NewsApi
import com.battisq.news.data.room.dao.NewsDao
import com.battisq.news.data.room.entities.NewsStoryEntity
import com.battisq.news.ui.list_news.recycler.NewsBoundaryCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get

class ListNewsViewModel(
    private val newsDao: NewsDao,
    pagingConfig: PagedList.Config? = null,
    private var boundaryCallback: NewsBoundaryCallback? = null
) :
    ViewModel(),
    KoinComponent {

    private val newsListDataSource = newsDao.getAllNews().create()
    val newsList = newsDao.getAllNews()
        .toLiveData(config = pagingConfig!!, boundaryCallback = boundaryCallback)

    fun addDB() {
        val api: NewsApi = get()

        viewModelScope.launch(Dispatchers.IO) {

            val list = mutableListOf<NewsStoryEntity>()
            val test = api.getNews(1).execute()
            val news = test.body()?.articles

            news!!.forEach { value ->
                list.add(NewsStoryEntity.create(value))
            }
            newsDao.insertMany(list)
        }
    }

    fun refresh(onSuccess: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            boundaryCallback?.page = 1
            newsDao.deleteAll()
            newsListDataSource.invalidate()
            viewModelScope.launch(Dispatchers.Main) { onSuccess() }
        }
    }

    fun retry(onSuccess: () -> Unit) {
        boundaryCallback?.page = boundaryCallback?.page?.plus(1)!!
        boundaryCallback?.fetchData()
        onSuccess()
    }

    companion object {

        @SuppressLint("NewApi")
        fun hasConnection(context: Context): Boolean {
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
    }
}