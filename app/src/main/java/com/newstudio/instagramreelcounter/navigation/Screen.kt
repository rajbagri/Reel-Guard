package com.newstudio.instagramreelcounter.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Usage : Screen("usage")
    object Analytics : Screen("analytics")
}