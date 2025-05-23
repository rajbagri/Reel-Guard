package com.newstudio.instagramreelcounter.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.newstudio.instagramreelcounter.DataStoreManager
import com.newstudio.instagramreelcounter.R
import kotlinx.coroutines.launch

@Composable
fun AppUsageCard(
    iconResId: Int,
    appName: String,
    usageTime: String
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var showReelCountPicker by remember { mutableStateOf(false) }
    val dataStore = DataStoreManager(context)
    var reelLimit by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        dataStore.getReelLimit(appName).collect{it ->
            reelLimit = it
        }
    }

    if (showReelCountPicker) {
        ReelCountPicker(
            currentCount = reelLimit,
            onDismiss = { showReelCountPicker = false },
            onCountSelected = { selected ->
                reelLimit = selected
                scope.launch {
                    dataStore.saveReelLimit(appName, reelLimit)
                }
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = iconResId,
                    contentDescription = "$appName icon",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(end = 16.dp)
                )

                Text(
                    text = appName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }


            Column(
                modifier = Modifier.padding(top = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier
                        .height(25.dp)
                        .clickable( onClick = {
                            showReelCountPicker = !showReelCountPicker
                        }),
                    painter = painterResource(R.drawable.reel),
                    contentDescription = "",
                    tint = Color.Gray
                )

                if(reelLimit > 0){
                    Text(
                        modifier = Modifier,
                        text = reelLimit.toString(),
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )
                }

            }

        }
    }
}


@Composable
@Preview(showBackground = true)
fun AppUsageCardPreview(){
    AppUsageCard(
        R.drawable.instagram_icon,
        "Instagram",
        "1:29"
    )
}