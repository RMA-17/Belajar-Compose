package com.rmaproject.jetnoteapp.data.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteModel(
    @PrimaryKey(autoGenerate = true) var id: Int? = 1,
    var title: String = "No Title",
    var content: String = "",
    var image: Bitmap?,
    var timeUpdated: Long = System.currentTimeMillis(),
    var timeCreated: Long
)
