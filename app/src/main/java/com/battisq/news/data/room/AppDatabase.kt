package com.battisq.news.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.battisq.news.data.room.converters.LocaleDateTimeConverter
import com.battisq.news.data.room.dao.NewsDao
import com.battisq.news.data.room.entities.NewsStory

@TypeConverters(LocaleDateTimeConverter::class)
@Database(entities = [NewsStory::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}