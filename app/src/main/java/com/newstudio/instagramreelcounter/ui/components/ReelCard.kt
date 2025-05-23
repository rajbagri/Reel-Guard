package com.newstudio.instagramreelcounter.ui.components


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Brush
import coil.compose.AsyncImage
import com.newstudio.instagramreelcounter.R
import com.newstudio.instagramreelcounter.ReelCardData


enum class CryptoCardStyle {
    Dark, Light
}

@Composable
fun ReelCard(
    reelCardData: ReelCardData,
    style: CryptoCardStyle = CryptoCardStyle.Dark,
    modifier: Modifier = Modifier,
    cardSize: Dp = 140.dp,
) {


    val bubbleColor = if (style == CryptoCardStyle.Dark) Color(0xFFf3f3f3) else Color(0xFF2C2C2C)
    val backgroundColor = if (style == CryptoCardStyle.Dark) Color.White else Color.Black
    val cardBackground = if (style == CryptoCardStyle.Dark) reelCardData.cardColor else reelCardData.cardColor
    val radius = 70

    Box(modifier = modifier.size(cardSize)) {

        Card(
            modifier = Modifier
                .size(cardSize)
                .clip(RoundedCornerShape(15.dp))
                .background(brush = Brush.linearGradient(reelCardData.cardColor)),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Canvas(modifier = Modifier.size(cardSize), onDraw = {
                drawRect(
                    color = Color(0xFF151414),
                    topLeft = Offset(x = size.width - radius + (radius * 0.2f), y = 12f),
                    size = size / 2f,
                )

                drawRect(
                    color = Color(0xFF151414),
                    topLeft = Offset(x = cardSize.value * 1.3f, y = cardSize.value * -1f),
                    size = size / 2f,
                )

                drawCircle(
                    color = Color(0xFF151414),
                    radius = cardSize.value / 1.5f,
                    center = Offset(
                        x = size.width - radius + (radius * 0.2f),
                        y = radius - (radius * 0.2f)
                    )
                )

                drawCircle(
                    brush = Brush.linearGradient(reelCardData.cardColor),
                    radius = radius * 0.8f,
                    center = Offset(
                        x = size.width / 2.14f,
                        y = radius - (radius * 0.2f)
                    )
                )

                drawCircle(
                    brush = Brush.linearGradient(reelCardData.cardColor),
                    radius = radius * 0.8f,
                    center = Offset(
                        x = size.width - radius + (radius * 0.2f),
                        y = radius + (radius * 1.93f)
                    )
                )

                drawCircle(
                    color = Color.DarkGray.copy(alpha = 0.5f),
                    radius = radius * 0.8f,
                    center = Offset(
                        x = size.width - radius + (radius * 0.2f),
                        y = radius - (radius * 0.2f)
                    )
                )
            })
        }

        Canvas(modifier = Modifier.size(cardSize), onDraw = {
            drawRect(
                color = Color(0xFF151414),
                topLeft = Offset(x = size.width - (cardSize.value / 2f) - 7.5f, y = 0f),
                size = size / 5f
            )

            drawCircle(
                color = Color.Transparent,
                radius = radius * 0.95f,
                center = Offset(x = size.width - radius + 5, y = radius - (radius * 0.05f))
            )
        })

        Box(
            modifier = Modifier
                .size((radius * 0.55f).dp) // Diameter = 2 * 0.8 * radius
                .align(Alignment.TopEnd)
                .offset(x = (-1).dp, y = 1.dp) // adjust based on bubble position
                .clip(CircleShape),
            contentAlignment = Alignment.Center // Changed to Center
        ) {
            AsyncImage(
                model = reelCardData.iconResId,
                contentDescription = null,
                contentScale = ContentScale.Crop, //  Added ContentScale.Crop
                modifier = Modifier.fillMaxSize()  // Added Modifier.fillMaxSize()
            )
        }



        Column(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterStart)
            ,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = reelCardData.reelCount.toString(),
                color = if (style == CryptoCardStyle.Dark) Color.White else Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = reelCardData.appName,
                color = if (style == CryptoCardStyle.Dark) Color.White else Color.Black,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )


        }
    }
}


@Composable
@Preview(showBackground = true)
fun ReelCardPreview() {
    val dummyCardColor = listOf(Color(0xFF833AB4), Color(0xFFF77737))

    ReelCard(
        reelCardData = ReelCardData(
            appName = "Instagram",
            reelCount = 1234L,
            watchHours = 56.7f,
            iconResId = R.drawable.instagram_icon,
            cardColor = dummyCardColor
        ),
        style = CryptoCardStyle.Dark
    )
}
