package com.newstudio.instagramreelcounter.navigation

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.newstudio.instagramreelcounter.ui.screen.AnalyticsScreen
import com.newstudio.instagramreelcounter.ui.screen.ReelCounterScreen
import com.newstudio.instagramreelcounter.ui.screens.AppUsageScreen
import com.newstudio.instagramreelcounter.viewmodel.ReelCardViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Navigation(
    values : PaddingValues,
    navController: NavHostController,
    reelCardViewModel: ReelCardViewModel,
) {

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            ReelCounterScreen(reelCardViewModel, values, navController)
        }

        composable(Screen.Usage.route) {
            AppUsageScreen(values)
        }

        composable(Screen.Analytics.route) {
            AnalyticsScreen(values)
        }
    }
}