package com.rmaproject.jetnoteapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rmaproject.jetnoteapp.data.converters.BitmapConverter
import com.rmaproject.jetnoteapp.data.model.NoteModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

@Database(entities = [NoteModel::class], version = 5)
@TypeConverters(BitmapConverter::class)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun dao() : NoteDatabaseDao

    companion object {
        @Volatile private var INSTANCE: NoteDatabase? = null
        fun getInstance(context:Context) : NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }
        }
        private fun buildDatabase(context: Context): NoteDatabase {
            return Room.databaseBuilder(context.applicationContext, NoteDatabase::class.java, "bookmark.db")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}