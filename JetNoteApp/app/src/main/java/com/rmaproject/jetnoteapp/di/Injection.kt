package com.rmaproject.jetnoteapp.di

import android.content.Context
import com.rmaproject.jetnoteapp.data.database.NoteDatabase
import com.rmaproject.jetnoteapp.data.repository.NotesRepository

object Injection {
    fun provideRepository(context: Context) : NotesRepository {
        val dao = NoteDatabase.getInstance(context).dao()
        return NotesRepository(dao)
    }
}