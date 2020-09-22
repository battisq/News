package com.battisq.news.data.room.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.battisq.news.data.json.NewsStory
import com.battisq.news.data.room.entities.NewsStoryEntity

@Dao
interface NewsDao {

    @Query("SELECT * FROM NewsStoryEntity ORDER BY date DESC")
    fun getAllNews() : DataSource.Factory<Int, NewsStoryEntity>

    @Query("SELECT * FROM NewsStoryEntity WHERE id BETWEEN :idStart AND :idEnd")
    fun getNewsInBetween(idStart: Int, idEnd: Int) : DataSource.Factory<Int, NewsStoryEntity>

    @Insert
    fun insert(newsStory: NewsStoryEntity)

    @Insert
    fun insertMany(news: List<NewsStoryEntity>)

    @Query("DELETE FROM NewsStoryEntity")
    fun deleteAll()
}