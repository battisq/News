package com.battisq.news.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.battisq.news.data.room.entities.NewsStoryEntity

@Dao
interface NewsDao {

    @Query("SELECT * FROM NewsStoryEntity")
    fun getAllNews() : LiveData<List<NewsStoryEntity>>

    @Insert
    fun insert(newsStory: NewsStoryEntity)

    @Delete
    fun delete(newsStory: NewsStoryEntity)
}