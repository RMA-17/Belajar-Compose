package com.rmaproject.jetnoteapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SearchField(
    query: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    displayHint: Boolean = true,
    onFocusState: (FocusState) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp),
        elevation = CardDefaults.cardElevation(
            4.dp, 4.dp, 8.dp, 8.dp
        ),
        border = BorderStroke(1.dp, Color.DarkGray),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = null
            )
            Box(
                modifier = Modifier.padding(start = 8.dp)
            ) {
                BasicTextField(
                    value = query,
                    onValueChange = { onValueChange(it) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.onFocusChanged {
                        onFocusState(it)
                    },
                )
                if (displayHint)
                    Text(
                        text = "Search note...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.DarkGray
                    )
            }
        }
    }
}


@Preview
@Composable
fun SearchFieldPreview() {
    SearchField("Mamang", {}, displayHint = false, onFocusState = {})
}