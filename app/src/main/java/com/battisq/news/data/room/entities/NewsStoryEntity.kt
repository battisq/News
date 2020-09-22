package com.battisq.news.data.room.entities

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.battisq.news.data.json.NewsStory
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity
data class NewsStoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val url: String,
//    val image: Bitmap? = null,
    val date: LocalDateTime
) {
    companion object {
        @SuppressLint("NewApi")
        fun create(newsStory: NewsStory) = NewsStoryEntity(
            title = newsStory.title,
            description = newsStory.description ?: "...",
            url = newsStory.url,
            date = LocalDateTime.parse(
                newsStory.publishedAt,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
            )
        )
    }
}