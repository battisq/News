package com.battisq.news.data.room.converters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import java.util.*

class BitmapConverter {
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromBitmap(image: Bitmap): String {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, baos)
        return Base64.getEncoder().encodeToString(baos.toByteArray())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toBitmap(string: String): Bitmap {
        val encodeByte: ByteArray = Base64.getDecoder().decode(string)
        return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
    }
}