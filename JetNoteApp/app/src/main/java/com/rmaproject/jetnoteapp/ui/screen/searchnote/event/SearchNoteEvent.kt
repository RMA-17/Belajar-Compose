package com.rmaproject.jetnoteapp.ui.screen.searchnote.event

import androidx.compose.ui.focus.FocusState

sealed class SearchNoteEvent {
    data class EnteredQuery(val value: String) : SearchNoteEvent()
    data class QueryChangeFocus(val focusState: FocusState): SearchNoteEvent()
    object FindNote: SearchNoteEvent()
}