package com.battisq.news.data.implementation

import com.battisq.news.data.api.NewsApi
import com.battisq.news.domain.entities.NewsStory
import com.battisq.news.domain.repositories.NewsApiRepository

class NewsApiRepositoryImpl(private val newsApi: NewsApi) : NewsApiRepository {
    override suspend fun getNews(page: Int): List<NewsStory>? {
        return newsApi
            .getNews(page = page)
            .execute()
            .body()
            ?.articles
            ?.map { it.toDomain() }
    }
}