package com.battisq.news.domain.interactors

import android.util.Log
import com.battisq.news.domain.entities.NewsStory
import com.battisq.news.domain.repositories.NewsApiRepository
import com.battisq.news.domain.repositories.NewsDatabaseRepository

class NewsDataInteractor(
    private val newsApiRepository: NewsApiRepository,
    private val newsDatabaseRepository: NewsDatabaseRepository
) {
    companion object {
        val TAG = this::class.simpleName
    }

    suspend fun fetchNews(page: Int) {
        var news: List<NewsStory>? = null

        try {
            news = newsApiRepository.getNews(page)
        } catch (ex: Exception) {
            Log.e(TAG, ex.message ?: "nothing")
            Log.e(TAG, ex.javaClass.name)
            Log.e(TAG, ex.stackTrace.joinToString { it.toString() })
        }

        if (news != null)
            newsDatabaseRepository.insertMany(news)
    }

    suspend fun getAllNews() = newsDatabaseRepository.getAllNews()
    suspend fun deleteAllNews() = newsDatabaseRepository.deleteAll()
}
