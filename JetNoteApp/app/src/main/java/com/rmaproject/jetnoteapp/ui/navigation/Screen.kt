package com.rmaproject.jetnoteapp.ui.navigation

sealed class Screen(val route: String) {
    object Dashboard: Screen("Dashboard")
    object ProfileScreen: Screen("Profile")
    object SearchNote: Screen("Search")
    object InsertNote: Screen("InsertNote?noteId={noteId}") {
        fun createRoute(noteId: Int?) = "InsertNote?noteId=$noteId"
    }
}