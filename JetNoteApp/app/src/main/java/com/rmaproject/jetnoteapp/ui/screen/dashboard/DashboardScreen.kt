package com.rmaproject.jetnoteapp.ui.screen.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rmaproject.jetnoteapp.R
import com.rmaproject.jetnoteapp.data.factory.ViewModelFactory
import com.rmaproject.jetnoteapp.data.model.NoteModel
import com.rmaproject.jetnoteapp.di.Injection
import com.rmaproject.jetnoteapp.ui.components.NoteGrid
import com.rmaproject.jetnoteapp.ui.screen.dashboard.state.NoteState
import com.rmaproject.jetnoteapp.ui.viewmodel.NotesViewModel

@Composable
fun DashboardScreen(
    navigateToInsertNote: (NoteModel) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
) {
    viewModel.noteState.collectAsState(initial = NoteState.Empty).value.let { state ->
        DashboardContent(
            state = state,
            modifier = modifier,
            navigateToInsertNote = navigateToInsertNote,
            viewModel = viewModel
        )
    }
}

@Composable
fun DashboardContent(
    state: NoteState<List<NoteModel>>,
    modifier: Modifier,
    navigateToInsertNote: (NoteModel) -> Unit,
    viewModel: NotesViewModel
) {
    when (state) {
        is NoteState.Empty -> {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = stringResource(R.string.msg_no_note),
                    style = MaterialTheme.typography.titleSmall,
                    textAlign = TextAlign.Center,
                )
            }
        }
        is NoteState.NotEmpty -> {
            NoteGrid(
                listNote = state.data,
                navigateToInsertNote = { navigateToInsertNote(it) },
                deleteNote = { viewModel.deleteNote(it) },
                modifier =  modifier
            )
        }
    }
}