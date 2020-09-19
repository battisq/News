package com.battisq.news.data.json

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class NewsStory(
    var source: Source,
    var author: Any?,
    var title: String,
    var description: String,
    var url: String,
    var urlToImage: String?,
    @SerializedName("publishedAt")
    var date: LocalDateTime,
    var content: String
)

data class Source(
    var id: Any? = null,
    var name: String? = null
)