package com.battisq.news.data.room.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

@Entity
data class NewsStoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    val url: String,
    val image: Bitmap,
    @SerializedName("publishedAt")
    val date: LocalDateTime
)