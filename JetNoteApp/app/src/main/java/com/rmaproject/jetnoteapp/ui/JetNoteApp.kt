package com.rmaproject.jetnoteapp.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rmaproject.jetnoteapp.ui.components.NoteAppBar
import com.rmaproject.jetnoteapp.ui.navigation.Screen
import com.rmaproject.jetnoteapp.ui.screen.dashboard.DashboardScreen
import com.rmaproject.jetnoteapp.ui.screen.insertnote.InsertNoteScreen
import com.rmaproject.jetnoteapp.ui.screen.profile.ProfileScreen
import com.rmaproject.jetnoteapp.ui.screen.searchnote.SearchNoteScreen

@Composable
fun JetNoteApp(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            if (currentRoute != Screen.InsertNote.route)
                NoteAppBar(
                    currentRoute = currentRoute.toString(),
                    navController = navController
                ) {
                    when (currentRoute) {
                        Screen.Dashboard.route -> {
                            IconButton(onClick = { navController.navigate(Screen.SearchNote.route) }) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Search Note"
                                )
                            }
                            IconButton(onClick = { navController.navigate(Screen.ProfileScreen.route) }) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = "Save Note"
                                )
                            }

                        }
                        else -> {}
                    }
                }
        },
        floatingActionButton = {
            if (currentRoute == Screen.Dashboard.route)
                FloatingActionButton(onClick = {
                    navController.navigate(Screen.InsertNote.route)
                }) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add new note"
                    )
                }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = modifier.padding(innerPadding),
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    navigateToInsertNote = {
                        navController.navigate(Screen.InsertNote.createRoute(it.id))
                    }
                )
            }
            composable(Screen.SearchNote.route) {
                SearchNoteScreen(
                    navigateToInsertNote = {
                        navController.navigate(Screen.InsertNote.createRoute(it.id))
                    }
                )
            }
            composable(Screen.ProfileScreen.route) {
                ProfileScreen()
            }
            composable(
                route = Screen.InsertNote.route,
                arguments = listOf(navArgument("noteId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
            ) {
                val noteId = it.arguments?.getInt("noteId") ?: -1
                InsertNoteScreen(
                    noteId = noteId,
                    navController = navController
                )
            }
        }
    }
}


@Preview
@Composable
fun JetNoteAppPreview() {
    JetNoteApp()
}