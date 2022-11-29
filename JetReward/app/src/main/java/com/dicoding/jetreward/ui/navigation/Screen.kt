package com.dicoding.jetreward.ui.navigation

sealed class Screen(val route: String) {
    object Home: Screen("Home")
    object Cart: Screen("Cart")
    object Profile: Screen("Profile")
}