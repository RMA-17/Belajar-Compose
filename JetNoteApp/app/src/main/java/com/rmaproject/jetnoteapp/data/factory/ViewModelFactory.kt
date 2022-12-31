package com.rmaproject.jetnoteapp.data.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rmaproject.jetnoteapp.data.repository.NotesRepository
import com.rmaproject.jetnoteapp.di.Injection
import com.rmaproject.jetnoteapp.ui.viewmodel.InsertNoteViewModel
import com.rmaproject.jetnoteapp.ui.viewmodel.NotesViewModel

class ViewModelFactory(
    private val repository: NotesRepository
): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(NotesViewModel::class.java) -> NotesViewModel(repository) as T
            modelClass.isAssignableFrom(InsertNoteViewModel::class.java) -> InsertNoteViewModel(repository) as T
            else -> throw Exception("Unknown ViewModel Class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE:ViewModelFactory?= null
        fun getInstance(context: Context) : ViewModelFactory = INSTANCE?: synchronized(this) {
            INSTANCE?: ViewModelFactory(Injection.provideRepository(context))
        }.also { INSTANCE = it }
    }
}