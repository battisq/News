package com.battisq.news.data.entities

import android.annotation.SuppressLint
import com.battisq.news.domain.entities.NewsStory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Дата-классы для хранения данных, полученных с NewsApi
 */

data class News(
    var status: String,
    var totalResults: Int,
    var articles: MutableList<Article>
)

data class Article(
    var source: Source,
    var author: Any?,
    var title: String?,
    var description: String?,
    var url: String,
    var urlToImage: String?,
    var publishedAt: String,
    var content: String
) {
    @SuppressLint("NewApi")
    fun toDomain(): NewsStory = NewsStory(
        url = url,
        title = title,
        description = description,
        imageToUrl = urlToImage,
        date = LocalDateTime.parse(
            publishedAt,
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        )
    )
}

data class Source(
    var id: Any? = null,
    var name: String? = null
)

