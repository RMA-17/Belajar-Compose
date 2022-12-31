package com.rmaproject.jetnoteapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rmaproject.jetnoteapp.data.model.NoteModel
import com.rmaproject.jetnoteapp.ui.screen.dashboard.NoteItem

@Composable
fun NoteGrid(
    listNote: List<NoteModel>,
    modifier: Modifier = Modifier,
    navigateToInsertNote: (NoteModel) -> Unit,
    deleteNote: (NoteModel) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(items = listNote, key = { it.id!! }) { note ->
            NoteItem(
                note = note,
                modifier = modifier.clickable { navigateToInsertNote(note) },
                setOnDeleteClickListener = { selectedNote ->
                    deleteNote(selectedNote)
                },
                image = note.image
            )
        }
    }
}