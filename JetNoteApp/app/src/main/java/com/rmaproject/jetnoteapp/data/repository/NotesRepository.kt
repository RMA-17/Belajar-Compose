package com.rmaproject.jetnoteapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rmaproject.jetnoteapp.data.database.NoteDatabaseDao
import com.rmaproject.jetnoteapp.data.model.NoteModel
import kotlinx.coroutines.flow.Flow

class NotesRepository(
    private val noteDao: NoteDatabaseDao
) {

    fun getAllNote(): Flow<List<NoteModel>> = noteDao.getAllNotes()
    suspend fun getNoteById(id: Int?) : NoteModel? = noteDao.getNoteById(id)
    fun searchNote(query: String) = noteDao.searchNote(query)

    suspend fun addNotes(note: NoteModel) {
        noteDao.insertNote(note)
    }

    suspend fun deleteNote(note: NoteModel) {
        noteDao.deleteNote(note)
    }

}