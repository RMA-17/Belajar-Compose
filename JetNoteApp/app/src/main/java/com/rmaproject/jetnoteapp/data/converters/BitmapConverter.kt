package com.rmaproject.jetnoteapp.data.converters

import android.R.attr
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream


class BitmapConverter {

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val resized = bitmap?.let {
            Bitmap.createScaledBitmap(
                it,
                (bitmap.width * 0.8).toInt(),
                (bitmap.height * 0.8).toInt(),
                true
            )
        }
        resized?.compress(Bitmap.CompressFormat.PNG, 10, outputStream)
        return outputStream.toByteArray()
    }

    @TypeConverter
    fun toBitmap(byteArray: ByteArray?): Bitmap? {
        return byteArray?.size?.let { BitmapFactory.decodeByteArray(byteArray, 0, it) }
    }
}