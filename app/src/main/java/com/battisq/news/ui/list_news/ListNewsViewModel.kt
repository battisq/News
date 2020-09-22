package com.battisq.news.ui.list_news

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.battisq.news.data.api.NewsApi
import com.battisq.news.data.room.dao.NewsDao
import com.battisq.news.data.room.entities.NewsStoryEntity
import com.battisq.news.ui.list_news.recycler.MainThreadExecutor
import com.battisq.news.ui.list_news.recycler.NewsBoundaryCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.get
import java.util.concurrent.Executor

class ListNewsViewModel(private val newsDao: NewsDao, pagingConfig: PagedList.Config? = null) :
    ViewModel(),
    KoinComponent {

    val newsList = newsDao.getAllNews()
        .toLiveData(config = pagingConfig!!, boundaryCallback = get<NewsBoundaryCallback>())

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
}