package com.battisq.news.data.room.dao

import androidx.paging.DataSource
import androidx.room.*
import com.battisq.news.data.room.entities.NewsStory

@Dao
interface NewsDao {

    @Query("SELECT * FROM NewsStory ORDER BY date DESC")
    fun getAllNews() : DataSource.Factory<Int, NewsStory>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(newsStory: NewsStory)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMany(news: List<NewsStory>)

    @Query("DELETE FROM NewsStory")
    fun deleteAll()
}