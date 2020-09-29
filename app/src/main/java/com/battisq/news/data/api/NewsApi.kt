package com.battisq.news.data.api

import com.battisq.news.data.json.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET(value ="https://newsapi.org/v2/everything?q=ios&from=2019-04-00&sortBy=publi" +
            "shedAt&apiKey=26eddb253e7840f988aec61f2ece2907")
    fun getNews(@Query("page") page: Int) : Call<News>
}