package com.battisq.news.data.api

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.battisq.news.data.json.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.time.LocalDate
import java.time.format.DateTimeFormatter

interface NewsApi {

    @SuppressLint("NewApi")
    @GET(
        value = "{endpoint}"
    )
    fun getNews(
        @Path("endpoint") endpoint: String = "everything",
        @Query("q") phrasesToSearch: String = "ios",
        @Query("from") from: String = "2019-04-00",
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("apiKey") apiKey: String = "26eddb253e7840f988aec61f2ece2907",
        @Query("page") page: Int = 1
    ): Call<News>
}