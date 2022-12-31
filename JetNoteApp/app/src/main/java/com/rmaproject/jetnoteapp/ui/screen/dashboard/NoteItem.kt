package com.rmaproject.jetnoteapp.ui.screen.dashboard

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.rmaproject.jetnoteapp.data.converters.ObjectConverters
import com.rmaproject.jetnoteapp.data.model.NoteModel

@Composable
fun NoteItem(
    note: NoteModel,
    modifier: Modifier = Modifier,
    image: Bitmap?,
    setOnDeleteClickListener: (NoteModel) -> Unit,
) {
    Card(
        modifier = modifier
            .width(160.dp)
            .height(
                if (image != null) 300.dp
                else 145.dp
            ),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(
            1.dp, Color.DarkGray
        )
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (image != null){
                    AsyncImage(
                        model = image,
                        contentDescription = "This note image",
                        modifier = Modifier.height(128.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = note.content,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier  = Modifier.height(12.dp))
                    Text(
                        text = "Last updated:\n${ObjectConverters.convertLongToDate(note.timeUpdated)}",
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            IconButton(
                onClick = { setOnDeleteClickListener(note) },
                Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete Note",
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun NoteItemPreview() {
    NoteItem(
        NoteModel(
            1, "This is title", "This is content", null, 2102312, 2102312
        ),
        image = null,
        setOnDeleteClickListener = {},
    )
}