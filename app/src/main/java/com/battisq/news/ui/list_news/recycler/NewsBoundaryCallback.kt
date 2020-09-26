package com.battisq.news.ui.list_news.recycler

import androidx.paging.PagedList
import androidx.paging.PagedList.BoundaryCallback
import com.battisq.news.data.api.NewsApi
import com.battisq.news.data.room.dao.NewsDao
import com.battisq.news.data.room.entities.NewsStoryEntity
import kotlin.concurrent.thread

class NewsBoundaryCallback(private val newsApi: NewsApi, private val newsDao: NewsDao) :
    BoundaryCallback<NewsStoryEntity>() {

    var page: Int = 1
        set(value) {
            if (field in 1..5)
                field = value
        }

    override fun onItemAtEndLoaded(itemAtEnd: NewsStoryEntity) {
        if (page in 1..4) {
            page++
            fetchData()
        }
    }

    override fun onZeroItemsLoaded() {
        fetchData()
    }

    private fun fetchData() {
        thread {
            val list = mutableListOf<NewsStoryEntity>()
            val news = newsApi.getNews(page).execute().body()?.articles

            news!!.forEach { value ->
                list.add(NewsStoryEntity.create(value))
            }
            newsDao.insertMany(list)
        }
    }
}