package com.battisq.news.data.implementation

import androidx.paging.DataSource
import com.battisq.news.data.room.dao.NewsDao
import com.battisq.news.domain.entities.NewsStory
import com.battisq.news.domain.repositories.NewsDatabaseRepository

class NewsDatabaseRepositoryImpl(private val newsDao: NewsDao): NewsDatabaseRepository {
    override suspend fun getAllNews(): DataSource.Factory<Int, NewsStory> {
        return newsDao.getAllNews()
    }

    override suspend fun insert(newsStory: NewsStory) {
        newsDao.insert(newsStory)
    }

    override suspend fun insertMany(news: List<NewsStory>) {
        newsDao.insertMany(news)
    }

    override suspend fun deleteAll() {
        newsDao.deleteAll()
    }
}