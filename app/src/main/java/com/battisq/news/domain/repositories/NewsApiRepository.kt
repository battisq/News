package com.battisq.news.domain.repositories

import com.battisq.news.domain.entities.NewsStory

interface NewsApiRepository {
    suspend fun getNews(page: Int): List<NewsStory>?
}