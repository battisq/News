package com.battisq.news.data.json

data class News(
    var status: String,
    var totalResults: Int,
    var articles: MutableList<NewsStory>
)

data class NewsStory(
    var source: Source,
    var author: Any?,
    var title: String?,
    var description: String?,
    var url: String,
    var urlToImage: String?,
    var publishedAt: String,
    var content: String
)

data class Source(
    var id: Any? = null,
    var name: String? = null
)