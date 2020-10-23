package com.battisq.news.domain.repositories

import androidx.paging.DataSource
import com.battisq.news.domain.entities.NewsStory

interface NewsDatabaseRepository {
    suspend fun getAllNews():  DataSource.Factory<Int, NewsStory>
    suspend fun insert(newsStory: NewsStory)
    suspend fun insertMany(news: List<NewsStory>)
    suspend fun deleteAll()
}