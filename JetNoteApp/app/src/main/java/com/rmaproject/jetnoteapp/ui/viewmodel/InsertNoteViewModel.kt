package com.rmaproject.jetnoteapp.ui.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmaproject.jetnoteapp.data.converters.ObjectConverters
import com.rmaproject.jetnoteapp.data.model.NoteModel
import com.rmaproject.jetnoteapp.data.repository.NotesRepository
import com.rmaproject.jetnoteapp.ui.screen.insertnote.event.InsertNoteEvent
import com.rmaproject.jetnoteapp.ui.screen.insertnote.state.TextFieldState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class InsertNoteViewModel(
    private val repository: NotesRepository
): ViewModel() {
    private val _noteTitle = mutableStateOf(
        TextFieldState(
            hint = "Type your note title here"
        )
    )
    val noteTitle: State<TextFieldState> = _noteTitle

    private val _noteContent = mutableStateOf(
        TextFieldState(
            hint = "Type some content for your note"
        )
    )
    val noteContent: State<TextFieldState> = _noteContent

    private val _insertEventFlow = MutableSharedFlow<InsertUiEvent>()
    val insertEventFlow = _insertEventFlow.asSharedFlow()

    private val _currentNoteImage = mutableStateOf<Bitmap?>(null)
    val currentNoteImage: State<Bitmap?> = _currentNoteImage

    private val _noteUpdatedAt = mutableStateOf<String?>(null)
    val noteUpdatedAt: State<String?> = _noteUpdatedAt

    private val _noteCreatedAt = mutableStateOf(System.currentTimeMillis())
    val noteCreatedAt: State<Long> = _noteCreatedAt

    private var currentNoteId: Int? = null

    fun setNoteState(noteId: Int) {
        if (noteId != -1)
            viewModelScope.launch {
                repository.getNoteById(noteId)?.also { note ->
                    currentNoteId = note.id
                    _noteTitle.value = noteTitle.value.copy(
                        text = note.title,
                        isHintVisible = false
                    )
                    _noteContent.value = noteContent.value.copy(
                        text = note.content,
                        isHintVisible = false
                    )
                    _currentNoteImage.value = note.image
                    _noteUpdatedAt.value = ObjectConverters.convertLongToDate(note.timeUpdated)
                    _noteCreatedAt.value = note.timeCreated
                }
            }
    }

    fun onEvent(event: InsertNoteEvent) {
        when (event) {
            is InsertNoteEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }
            is InsertNoteEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank(),
                )
            }
            is InsertNoteEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }
            is InsertNoteEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteContent.value.text.isBlank(),
                )
            }
            is InsertNoteEvent.ChangeNoteImage -> {
                _currentNoteImage.value = event.noteImage
            }
            is InsertNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        repository.addNotes(
                            NoteModel(
                                title = _noteTitle.value.text,
                                content = _noteContent.value.text,
                                timeUpdated = System.currentTimeMillis(),
                                id = currentNoteId,
                                image = _currentNoteImage.value,
                                timeCreated = _noteCreatedAt.value
                            )
                        )
                        _insertEventFlow.emit(InsertUiEvent.SaveNote)
                    } catch (e: Exception) {
                        _insertEventFlow.emit(
                            InsertUiEvent.ShowSnackbar(
                                message = e.message ?: "Error occurred, cannot save note"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class InsertUiEvent {
        data class ShowSnackbar(val message: String) : InsertUiEvent()
        object SaveNote : InsertUiEvent()
    }
}