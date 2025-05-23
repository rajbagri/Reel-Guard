package com.newstudio.instagramreelcounter

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.Modifier
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.newstudio.instagramreelcounter.ui.theme.InstagramReelCounterTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.newstudio.instagramreelcounter.navigation.Navigation
import com.newstudio.instagramreelcounter.ui.animatewrapper.AnimatedCardWrapper
import com.newstudio.instagramreelcounter.ui.animatewrapper.AnimatedUsageCardWrapper
import com.newstudio.instagramreelcounter.ui.components.AppUsageCard
import com.newstudio.instagramreelcounter.ui.components.BottomNavigation
import com.newstudio.instagramreelcounter.ui.components.NavigationDrawer
import com.newstudio.instagramreelcounter.ui.components.ReelCard
import com.newstudio.instagramreelcounter.ui.screen.AnalyticsScreen
import com.newstudio.instagramreelcounter.ui.screens.AppUsageScreen
import com.newstudio.instagramreelcounter.viewmodel.ReelCardViewModel
import com.newstudio.instagramreelcounter.viewmodel.UsageViewModel


    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Set the UI content
        setContent {
            InstagramReelCounterTheme {

                var selectedIndex by remember { mutableStateOf(0) }
                val navController = rememberNavController()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                NavigationDrawer(
                    drawerState = drawerState
                ) {

                    Scaffold(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = Color.Black),
                        containerColor = Color.Black,
                        bottomBar = {
                            BottomNavigation(
                                selectedIndex = selectedIndex,
                                onItemSelected = { selectedIndex = it },
                                navController = navController
                            )
                        },
                        topBar = {
                            TopAppBar(
                               title = { Text(color = Color.White, text = "Monitor") },

                                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.DarkGray.copy(alpha = 0.2f))
                            )
                        }
                    ) {
                        val context = LocalContext.current.applicationContext as Application
                        val viewModel = ReelCardViewModel(context)
                        Navigation(navController = navController, reelCardViewModel = viewModel, values = it)
                    }
                }
            }
        }

        logComAndroidPackages()
    }

    private fun logComAndroidPackages() {
        val pm = packageManager
        val packages = pm.getInstalledPackages(PackageManager.GET_META_DATA)

        for (packageInfo in packages) {
            val packageName = packageInfo.packageName
            if(packageName.contains("instagram")){

            Log.d("ComAndroidLogger", "Package: $packageName")
            }


        }
    }
}






@RequiresApi(Build.VERSION_CODES.O)
fun getIcon(
    context: Context,
    packageName: String,
    fallbackResId: Int
): AdaptiveIconDrawable {
    val drawable: Drawable = try {
        if (isAppInstalled(context, packageName)) {
            context.packageManager.getApplicationIcon(packageName)
        } else {
            ContextCompat.getDrawable(context, fallbackResId)!!
        }
    } catch (e: PackageManager.NameNotFoundException) {
        Log.e("AppIcon", "$packageName not found.", e)
        ContextCompat.getDrawable(context, fallbackResId)!!
    }

    return if (drawable is AdaptiveIconDrawable) {
        drawable
    } else {
        Log.e("AppIcon", "Icon for $packageName is not an AdaptiveIconDrawable. Using fallback.")
        ContextCompat.getDrawable(context, fallbackResId) as AdaptiveIconDrawable
    }
}


fun isAppInstalled(context: Context, packageName: String): Boolean {
    val packageManager = context.packageManager
    return try {
        packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}



