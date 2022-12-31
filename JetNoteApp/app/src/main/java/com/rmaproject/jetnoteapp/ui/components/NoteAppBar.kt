package com.rmaproject.jetnoteapp.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.rmaproject.jetnoteapp.ui.navigation.Screen

@Composable
fun NoteAppBar(
    currentRoute: String?,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    actions: @Composable (RowScope.() -> Unit)
) {
    TopAppBar(
        title = {
            Text(
                text =
                when (currentRoute) {
                    Screen.InsertNote.route -> "Insert Note"
                    Screen.Dashboard.route -> "Dashboard"
                    Screen.ProfileScreen.route -> "Profile"
                    Screen.SearchNote.route -> "Search Note"
                    else -> "Unknown Route"
                },
                textAlign = TextAlign.Center,
            )
        },
        modifier = modifier,
        navigationIcon = {
            if (currentRoute != Screen.Dashboard.route)
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        Icons.Default.ArrowBackIosNew,
                        contentDescription = "Navigate up"
                    )
                }
        },
        actions = actions
    )
}