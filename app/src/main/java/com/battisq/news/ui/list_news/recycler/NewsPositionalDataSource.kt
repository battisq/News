package com.battisq.news.ui.list_news.recycler

import androidx.paging.LivePagedListBuilder
import androidx.paging.PositionalDataSource
import androidx.paging.toLiveData
import com.battisq.news.data.room.dao.NewsDao
import com.battisq.news.data.room.entities.NewsStoryEntity

class NewsPositionalDataSource(private val newsDao: NewsDao) :
    PositionalDataSource<NewsStoryEntity>() {


    override fun loadInitial(
        params: LoadInitialParams,
        callback: LoadInitialCallback<NewsStoryEntity>
    ) {
        val result =
            newsDao.getNewsInBetween(params.requestedStartPosition, params.requestedLoadSize)
        val list = result.toLiveData(pageSize = 5).value ?: listOf<NewsStoryEntity>()
        callback.onResult(list, 0)
    }

    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<NewsStoryEntity>) {
        val result =
            newsDao.getNewsInBetween(params.startPosition, params.loadSize)
        callback.onResult(result.toLiveData(pageSize = 5).value!!)
    }
}