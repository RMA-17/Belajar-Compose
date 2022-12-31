package com.rmaproject.jetnoteapp.data.database

import androidx.room.*
import com.rmaproject.jetnoteapp.data.model.NoteModel
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteModel)

    @Delete
    suspend fun deleteNote(note: NoteModel)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): Flow<List<NoteModel>>

    @Query("SELECT * FROM notes WHERE id = :noteId")
    suspend fun getNoteById(noteId: Int?): NoteModel?

    @Query("SELECT * FROM notes WHERE title LIKE '%' ||:searchQuery|| '%' OR content LIKE '%' ||:searchQuery|| '%'")
    fun searchNote(searchQuery: String) : Flow<List<NoteModel>>
}