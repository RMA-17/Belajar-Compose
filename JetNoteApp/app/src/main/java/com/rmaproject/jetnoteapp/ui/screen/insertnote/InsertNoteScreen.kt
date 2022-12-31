package com.rmaproject.jetnoteapp.ui.screen.insertnote

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.rmaproject.jetnoteapp.data.converters.ObjectConverters
import com.rmaproject.jetnoteapp.data.factory.ViewModelFactory
import com.rmaproject.jetnoteapp.di.Injection
import com.rmaproject.jetnoteapp.ui.components.CustomTextField
import com.rmaproject.jetnoteapp.ui.components.NoteAppBar
import com.rmaproject.jetnoteapp.ui.navigation.Screen
import com.rmaproject.jetnoteapp.ui.screen.insertnote.event.InsertNoteEvent
import com.rmaproject.jetnoteapp.ui.viewmodel.InsertNoteViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun InsertNoteScreen(
    noteId: Int,
    modifier: Modifier = Modifier,
    viewModel: InsertNoteViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(LocalContext.current))
    ),
    navController: NavHostController
) {

    viewModel.setNoteState(noteId)
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.insertEventFlow.collectLatest { event ->
            when (event) {
                is InsertNoteViewModel.InsertUiEvent.SaveNote -> {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(navController.graph.findStartDestination().id)
                    }
                }
                is InsertNoteViewModel.InsertUiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        event.message
                    )
                }
            }
        }
    }

    InsertNoteContent(
        modifier = modifier,
        viewModel = viewModel,
        scaffoldState = scaffoldState,
        noteId = noteId,
        navController = navController
    )
}

@Composable
fun InsertNoteContent(
    viewModel: InsertNoteViewModel,
    modifier: Modifier = Modifier,
    scaffoldState: ScaffoldState,
    noteId: Int,
    navController: NavHostController
) {
    val context = LocalContext.current
    val currentNoteImage = viewModel.currentNoteImage
    val photoSelector = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            val bitmap = ObjectConverters.convertUriToBitmap(
                uri ?: return@rememberLauncherForActivityResult,
                context
            )
            viewModel.onEvent(InsertNoteEvent.ChangeNoteImage(bitmap))
        }
    )
    val imageContentModifier = Modifier
        .fillMaxWidth()
        .height(256.dp)
        .clickable {
            photoSelector.launch(
                PickVisualMediaRequest(mediaType = ImageOnly)
            )
        }
    val currentNoteUpdatedDate = viewModel.noteUpdatedAt.value
    val currentNoteCreatedDate = viewModel.noteCreatedAt.value
    Scaffold(
        modifier = modifier,
        topBar = {
            NoteAppBar(
                currentRoute = Screen.InsertNote.route, navController = navController
            ) {
                IconButton(onClick = { viewModel.onEvent(InsertNoteEvent.SaveNote) }) {
                    Icon(
                        Icons.Default.Save,
                        contentDescription = "Save Note"
                    )
                }
            }
        },
        scaffoldState = scaffoldState
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (currentNoteImage.value != null)
                AsyncImage(
                    model = currentNoteImage.value,
                    contentDescription = "This note Image",
                    modifier = imageContentModifier,
                    contentScale = ContentScale.Crop
                )
            else Icon(
                Icons.Default.Image,
                contentDescription = "Add Image",
                modifier = imageContentModifier
            )
            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Column {
                    if (noteId != -1) {
                        Text(
                            text = "Created At: ${ObjectConverters.convertLongToDate(currentNoteCreatedDate)}",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    if (!currentNoteUpdatedDate.isNullOrEmpty()) {
                        Text(
                            text = "Updated At: $currentNoteUpdatedDate",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            TextFields(
                viewModel = viewModel,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun TextFields(
    viewModel: InsertNoteViewModel,
    modifier: Modifier = Modifier
) {
    val titleState = viewModel.noteTitle
    val contentState = viewModel.noteContent
    Column(
        modifier = modifier
    ) {
        CustomTextField(
            text = titleState.value.text,
            hint = titleState.value.hint,
            onValueChange = {
                viewModel.onEvent(InsertNoteEvent.EnteredTitle(it))
            },
            onFocusState = {
                viewModel.onEvent(InsertNoteEvent.ChangeTitleFocus(it))
            },
            isHintVisible = titleState.value.isHintVisible,
            singleLine = true,
            textStyle = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(24.dp))
        CustomTextField(
            text = contentState.value.text,
            hint = contentState.value.hint,
            onValueChange = {
                viewModel.onEvent(InsertNoteEvent.EnteredContent(it))
            },
            onFocusState = {
                viewModel.onEvent(InsertNoteEvent.ChangeContentFocus(it))
            },
            isHintVisible = contentState.value.isHintVisible,
            singleLine = false,
            textStyle = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxHeight()
        )
    }
}


@Preview
@Composable
fun InsertNoteScreenPreview() {
    InsertNoteScreen(1, navController = rememberNavController())
}