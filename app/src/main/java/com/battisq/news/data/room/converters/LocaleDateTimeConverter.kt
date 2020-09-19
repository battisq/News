package com.battisq.news.data.room.converters

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDateTime

class LocaleDateTimeConverter {
    @TypeConverter
    fun fromLocaleDateTime(date: LocalDateTime): String {
        return date.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toLocaleDateTime(timestamp: String): LocalDateTime {
        return LocalDateTime.parse(timestamp)
    }
}