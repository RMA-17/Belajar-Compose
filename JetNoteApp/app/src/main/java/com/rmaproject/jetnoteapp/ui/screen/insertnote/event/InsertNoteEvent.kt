package com.rmaproject.jetnoteapp.ui.screen.insertnote.event

import android.graphics.Bitmap
import androidx.compose.ui.focus.FocusState

sealed class InsertNoteEvent {
    data class EnteredTitle(val value: String): InsertNoteEvent()
    data class ChangeTitleFocus(val focusState: FocusState): InsertNoteEvent()
    data class EnteredContent(val value: String): InsertNoteEvent()
    data class ChangeContentFocus(val focusState: FocusState): InsertNoteEvent()
    data class ChangeNoteImage(val noteImage: Bitmap?): InsertNoteEvent()
    object SaveNote: InsertNoteEvent()
}