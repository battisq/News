package com.battisq.news.ui.list_news.recycler

import androidx.paging.PagedList
import androidx.paging.PagedList.BoundaryCallback
import com.battisq.news.data.api.NewsApi
import com.battisq.news.data.room.dao.NewsDao
import com.battisq.news.data.room.entities.NewsStoryEntity
import kotlin.concurrent.thread

class NewsBoundaryCallback(private val newsApi: NewsApi, private val newsDao: NewsDao) :
    BoundaryCallback<NewsStoryEntity>() {

    override fun onItemAtEndLoaded(itemAtEnd: NewsStoryEntity) {
        if (page in 1..4)
            fetchData(++page)
    }

    override fun onZeroItemsLoaded() {
        fetchData(page)
    }

    private fun fetchData(page: Int) {
        thread {
            val list = mutableListOf<NewsStoryEntity>()
            val test = newsApi.getNews(page).execute()
            val news = test.body()?.articles

            news!!.forEach { value ->
                list.add(NewsStoryEntity.create(value))
            }
            newsDao.insertMany(list)
        }
    }

    companion object {
        var page: Int = 1
    }
}