package com.newstudio.instagramreelcounter.ui.screen

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.newstudio.instagramreelcounter.DataStoreManager
import com.newstudio.instagramreelcounter.R
import com.newstudio.instagramreelcounter.ReelCardData
import com.newstudio.instagramreelcounter.ui.animatewrapper.AnimatedCardWrapper
import com.newstudio.instagramreelcounter.ui.animatewrapper.AnimatedUsageCardWrapper
import com.newstudio.instagramreelcounter.ui.components.AppUsageCard
import com.newstudio.instagramreelcounter.ui.components.ReelCard
import com.newstudio.instagramreelcounter.viewmodel.ReelCardViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReelCounterScreen(
    viewModel: ReelCardViewModel,
    values : PaddingValues,
    navController: NavController
) {
    val context = LocalContext.current
    val dataStore = DataStoreManager(context)
    val scope = rememberCoroutineScope()

    val instagram by viewModel.instagramReel.collectAsState()
    val youtube by viewModel.youtubeReel.collectAsState()
    val snapchat by viewModel.snapchatReel.collectAsState()
    val facebook by viewModel.facebookReel.collectAsState()

    val cards = listOf(
        ReelCardData("Instagram", instagram, 0f, R.drawable.instagram_icon, listOf(Color(0xFF833AB4), Color(0xFFF77737))),
        ReelCardData("Youtube", youtube, 0f, R.drawable.youtube, listOf(Color(0xFFB31217), Color(0xFFFF0000))),
        ReelCardData("Snapchat", snapchat, 0f, R.drawable.snapchat, listOf(Color(0xFFFFFF00), Color(0xFFFFF700))),
        ReelCardData("Facebook", facebook, 0f, R.drawable.facebook, listOf(Color(0xFF1877F2), Color(0xFF42A5F5)))
    )


    Column(
        modifier = Modifier.
        padding(top = values.calculateTopPadding())
    ) {
        var globalIndex = 0 // Global index for animation delay

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.DarkGray.copy(alpha = 0.2f),
                            Color.DarkGray.copy(alpha = 0.4f),
                        )
                    ),
                    shape = RoundedCornerShape(bottomEnd = 40.dp, bottomStart = 40.dp)
                )
                .clip(RoundedCornerShape(bottomEnd = 40.dp, bottomStart = 40.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(bottomEnd = 50.dp, bottomStart = 50.dp)
        ){
            for (i in cards.indices step 2) {
                Row(
                    modifier = Modifier
                        .padding(bottom = 25.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    AnimatedCardWrapper(index = globalIndex++) {
                        ReelCard(cards[i])
                    }
                    if (i + 1 < cards.size) {
                        AnimatedCardWrapper(index = globalIndex++) {
                            ReelCard(cards[i + 1])
                        }
                    }
                }
            }
        }

        val appUsages = listOf(
            Triple(R.drawable.instagram_icon, "Instagram", "1:30"),
            Triple(R.drawable.youtube, "YouTube", "1:30"),
            Triple(R.drawable.snapchat, "Snapchat", "1:30"),
            Triple(R.drawable.facebook, "Facebook", "1:30")
        )

        Text(
            text = "Reel Count",
            modifier = Modifier.padding(start = 20.dp, top = 15.dp, bottom = 5.dp),
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        for ((icon, name, time) in appUsages) {
            AnimatedUsageCardWrapper(index = globalIndex++) {
                AppUsageCard(icon, name, time)
            }
        }

    }
}
