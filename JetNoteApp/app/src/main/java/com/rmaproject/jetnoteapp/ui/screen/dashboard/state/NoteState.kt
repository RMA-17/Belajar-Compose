package com.rmaproject.jetnoteapp.ui.screen.dashboard.state

sealed class NoteState<out T: Any?> {

    data class NotEmpty<out T: Any>(val data: T) : NoteState<T>()

    object Empty : NoteState<Nothing>()
}