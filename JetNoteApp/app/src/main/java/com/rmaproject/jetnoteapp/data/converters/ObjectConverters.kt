package com.rmaproject.jetnoteapp.data.converters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.text.SimpleDateFormat
import java.util.*

object ObjectConverters {
    fun convertUriToBitmap(
        uri: Uri?,
        context: Context
    ): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(context.contentResolver,
                    uri ?: Uri.EMPTY
                ))
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    }

    fun convertLongToDate(timeMillis: Long) : String {
        val date = Date(timeMillis)
        val sdf = SimpleDateFormat("dd MMMM yyyy - HH:mm", Locale.getDefault()).format(date)
        val splitSdF = sdf.split("-")
        return "${splitSdF[0]} at ${splitSdF[1]}"
    }
}