package com.newstudio.instagramreelcounter.ui.components

import android.widget.TimePicker
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay


@Composable
fun TimePicker(
    onDismiss: () -> Unit,
    onTimeSelected: (Int, Int) -> Unit,
    initialHours: Int = 0,
    initialMinutes: Int = 30
) {
    var selectedHour by remember { mutableStateOf(initialHours) }
    var selectedMinute by remember { mutableStateOf(initialMinutes) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Set app limit") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("This app limit will reset at midnight")

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    NumberPicker(
                        value = selectedHour,
                        range = 0..12,
                        label = "hr"
                    ) { selectedHour = it }

                    NumberPicker(
                        value = selectedMinute,
                        range = 0..59 step 5,
                        label = "mins"
                    ) { selectedMinute = it }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onTimeSelected(selectedHour, selectedMinute)
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


@Composable
fun NumberPicker(
    value: Int,
    range: IntProgression,
    label: String,
    onValueChange: (Int) -> Unit
) {
    val list = remember { range.toList() }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 14.sp)

        LazyColumn(
            modifier = Modifier
                .height(120.dp)
                .width(60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(list.size) { index ->
                val item = list[index]
                Text(
                    text = item.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onValueChange(item) },
                    textAlign = TextAlign.Center,
                    fontSize = if (item == value) 20.sp else 16.sp,
                    color = if (item == value) MaterialTheme.colorScheme.primary else Color.Gray
                )
            }
        }
    }
}
