package com.battisq.news.domain.entities

import android.annotation.SuppressLint
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.battisq.news.data.entities.Article
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity
data class NewsStory(
    @PrimaryKey
    val url: String,
    val title: String?,
    val description: String?,
    val imageToUrl: String?,
    val date: LocalDateTime
) {
    companion object {
        @SuppressLint("NewApi")
        fun create(article: Article) = NewsStory(
            title = article.title ?: "",
            description = article.description ?: "...",
            url = article.url,
            date = LocalDateTime.parse(
                article.publishedAt,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            ),
            imageToUrl = article.urlToImage
        )
    }
}