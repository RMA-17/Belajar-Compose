package com.rmaproject.jetnoteapp.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmaproject.jetnoteapp.data.model.NoteModel
import com.rmaproject.jetnoteapp.data.repository.NotesRepository
import com.rmaproject.jetnoteapp.ui.screen.dashboard.state.NoteState
import com.rmaproject.jetnoteapp.ui.screen.insertnote.event.InsertNoteEvent
import com.rmaproject.jetnoteapp.ui.screen.insertnote.state.TextFieldState
import com.rmaproject.jetnoteapp.ui.screen.searchnote.event.SearchNoteEvent
import com.rmaproject.jetnoteapp.ui.screen.searchnote.state.SearchFieldState
import com.rmaproject.jetnoteapp.ui.viewmodel.NotesViewModel.SearchUiEvent.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NotesViewModel(
    private val repository: NotesRepository
): ViewModel() {

    private val _noteState: MutableStateFlow<NoteState<List<NoteModel>>> = MutableStateFlow(
        NoteState.Empty)
    val noteState: StateFlow<NoteState<List<NoteModel>>>
        get() = _noteState

    private val _searchQuery = mutableStateOf(SearchFieldState())
    val searchQuery: State<SearchFieldState> = _searchQuery

    private val _searchEventFlow = MutableSharedFlow<SearchUiEvent>()
    val searchEventFlow = _searchEventFlow.asSharedFlow()

    private fun getAllNote() {
        viewModelScope.launch {
            repository.getAllNote().collect { noteList ->
                if (noteList.isNotEmpty()) _noteState.value = NoteState.NotEmpty(noteList)
                else _noteState.value = NoteState.Empty
            }
        }
    }

    fun onSearchNoteEvent(event: SearchNoteEvent) {
        when (event) {
            is SearchNoteEvent.EnteredQuery -> {
                _searchQuery.value = searchQuery.value.copy(
                    text = event.value
                )
            }
            is SearchNoteEvent.QueryChangeFocus -> {
                _searchQuery.value = searchQuery.value.copy(
                    isHintVisible = !event.focusState.isFocused && searchQuery.value.text.isBlank()
                )
            }
            is SearchNoteEvent.FindNote -> {
                viewModelScope.launch {
                    if (_searchQuery.value.text.isNotBlank() || _searchQuery.value.text.isNotEmpty()) {
                        repository.searchNote(_searchQuery.value.text)
                            .catch { e ->
                                _searchEventFlow.emit(Error(e.message ?: "Error Occurred"))
                            }
                            .collect { noteList ->
                                if (noteList.isEmpty()) _searchEventFlow.emit(NotFoundNote)
                                else _searchEventFlow.emit(FoundNote(noteList))
                            }
                    } else {
                        repository.getAllNote().collect { listNote ->
                            if (listNote.isNotEmpty())
                                _searchEventFlow.emit(EmptyQuery(listNote))
                            else _searchEventFlow.emit(EmptyNote)
                        }
                    }
                }
            }
        }
    }

    init {
        getAllNote()
    }

    sealed class SearchUiEvent {
        data class Error(val message: String) : SearchUiEvent()
        data class FoundNote(val listNote: List<NoteModel>) : SearchUiEvent()
        object NotFoundNote : SearchUiEvent()
        data class EmptyQuery(val listNote: List<NoteModel>) : SearchUiEvent()
        object EmptyNote : SearchUiEvent()
    }

    fun insertNote(note: NoteModel) = viewModelScope.launch{ repository.addNotes(note) }
    fun deleteNote(note: NoteModel) = viewModelScope.launch { repository.deleteNote(note) }
}