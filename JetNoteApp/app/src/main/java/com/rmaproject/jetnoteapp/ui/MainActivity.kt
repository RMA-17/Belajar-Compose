package com.rmaproject.jetnoteapp.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rmaproject.jetnoteapp.R
import com.rmaproject.jetnoteapp.data.factory.ViewModelFactory
import com.rmaproject.jetnoteapp.data.kotpref.FirstLaunchState
import com.rmaproject.jetnoteapp.data.model.NoteModel
import com.rmaproject.jetnoteapp.ui.theme.AppTheme
import com.rmaproject.jetnoteapp.ui.viewmodel.NotesViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: NotesViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    JetNoteApp()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (FirstLaunchState.isAppInitialised) {
            resources.getStringArray(R.array.initial_note_content).forEachIndexed { index, noteContent ->
                val contentList = noteContent.split(":")
                val imageFromList = getImageList()[index]
                val imageBitmap = BitmapFactory.decodeResource(resources, imageFromList)
                val title = contentList[0]
                val content = if (contentList.size > 1) contentList[1] else ""
                viewModel.insertNote(
                    NoteModel(
                        id = null,
                        title = title,
                        content = content,
                        timeUpdated = System.currentTimeMillis(),
                        timeCreated = System.currentTimeMillis(),
                        image = imageBitmap
                    )
                )
            }
            FirstLaunchState.isAppInitialised = false
        }
    }

    private fun getImageList(): List<Int> {
        return listOf(
            R.drawable.gambar_1,
            R.drawable.gambar_2,
            R.drawable.gambar_3,
            R.drawable.gambar_4,
            R.drawable.gambar_5,
            R.drawable.gambar_6,
            R.drawable.gambar_7,
            R.drawable.gambar_8,
            R.drawable.gambar_9,
            R.drawable.gambar_10,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        JetNoteApp()
    }
}