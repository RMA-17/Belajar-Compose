package com.rmaproject.jetnoteapp.ui.screen.searchnote

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rmaproject.jetnoteapp.R
import com.rmaproject.jetnoteapp.data.factory.ViewModelFactory
import com.rmaproject.jetnoteapp.data.model.NoteModel
import com.rmaproject.jetnoteapp.di.Injection
import com.rmaproject.jetnoteapp.ui.components.NoteGrid
import com.rmaproject.jetnoteapp.ui.components.SearchField
import com.rmaproject.jetnoteapp.ui.screen.searchnote.event.SearchNoteEvent
import com.rmaproject.jetnoteapp.ui.screen.searchnote.state.SearchFieldState
import com.rmaproject.jetnoteapp.ui.viewmodel.NotesViewModel
import com.rmaproject.jetnoteapp.ui.viewmodel.NotesViewModel.SearchUiEvent
import com.rmaproject.jetnoteapp.ui.viewmodel.NotesViewModel.SearchUiEvent.*

@Composable
fun SearchNoteScreen(
    navigateToInsertNote: (NoteModel) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
) {
    val searchFieldState = viewModel.searchQuery.value
    LaunchedEffect(key1 = searchFieldState.text) {
        viewModel.onSearchNoteEvent(SearchNoteEvent.FindNote)
    }
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SearchField(
            query = searchFieldState.text,
            onValueChange = {
                viewModel.onSearchNoteEvent(SearchNoteEvent.EnteredQuery(it))
                if (it.isNotBlank() || it.isNotEmpty()) {
                    viewModel.onSearchNoteEvent(SearchNoteEvent.FindNote)
                }
            },
            onFocusState = {
                viewModel.onSearchNoteEvent(SearchNoteEvent.QueryChangeFocus(it))
            },
            displayHint = searchFieldState.isHintVisible
        )
        viewModel.searchEventFlow.collectAsState(initial = EmptyNote).value.let { event ->
            SearchContent(
                viewModel = viewModel,
                navigateToInsertNote = navigateToInsertNote,
                event = event,
                searchFieldState = searchFieldState
            )
        }
    }
}

@Composable
fun SearchContent(
    viewModel: NotesViewModel,
    navigateToInsertNote: (NoteModel) -> Unit,
    searchFieldState: SearchFieldState,
    event: SearchUiEvent,
    modifier: Modifier = Modifier,
) {
    when (event) {
        is Error -> {
            ErrorOrBlankContent(
                message = event.message,
                modifier = modifier
            )
        }
        is FoundNote -> {
            FoundNoteContent(
                listNote = event.listNote,
                navigateToInsertNote = navigateToInsertNote,
                deleteNote = { viewModel.deleteNote(it) },
                modifier = modifier.padding(8.dp)
            )
        }
        is NotFoundNote -> {
            ErrorOrBlankContent(
                message = buildString {
                    append(stringResource(R.string.msg_search_not_found))
                    append(searchFieldState.text)
                },
                modifier = modifier
            )
        }
        is EmptyNote -> {
            ErrorOrBlankContent(
                message = stringResource(R.string.msg_no_note),
                modifier = modifier
            )
        }
        is EmptyQuery -> {
            FoundNoteContent(
                listNote = event.listNote,
                navigateToInsertNote = navigateToInsertNote,
                deleteNote = { viewModel.deleteNote(it) },
                modifier = modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun ErrorOrBlankContent(
    modifier: Modifier = Modifier,
    message: String,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun FoundNoteContent(
    listNote: List<NoteModel>,
    navigateToInsertNote: (NoteModel) -> Unit,
    deleteNote: (NoteModel) -> Unit,
    modifier: Modifier = Modifier
) {
    NoteGrid(
        listNote = listNote,
        navigateToInsertNote = { navigateToInsertNote(it) },
        deleteNote = { deleteNote(it) },
        modifier = modifier
    )
}

@Preview
@Composable
fun SearchNoteScreenPreview() {
    SearchNoteScreen({})
}