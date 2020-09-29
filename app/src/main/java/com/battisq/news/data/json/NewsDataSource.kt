package com.battisq.news.data.json

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
)

data class Source(
    var id: Any? = null,
    var name: String? = null
)