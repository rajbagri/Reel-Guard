package com.newstudio.instagramreelcounter.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ReelCountPicker(
    currentCount: Int = 50,
    onDismiss: () -> Unit,
    onCountSelected: (Int) -> Unit
) {
    val countOptions = listOf(0) + (1..20).map { it * 50 } // 0, 50, ..., 1000
    val defaultIndex = countOptions.indexOf(currentCount).takeIf { it >= 0 } ?: 0
    var selectedIndex by remember { mutableStateOf(defaultIndex) }

    var isEditing by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf(TextFieldValue(countOptions[selectedIndex].toString())) }
    val focusManager = LocalFocusManager.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Set Reel Watch Limit",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (countOptions[selectedIndex] == 0)
                        "No limit set"
                    else
                        "Limit: ${countOptions[selectedIndex]} reels",
                    fontSize = 18.sp
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = {
                            if (selectedIndex > 0) selectedIndex--
                            inputText = TextFieldValue(countOptions[selectedIndex].toString())
                        }
                    ) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Decrease")
                    }

                    if (isEditing) {
                        OutlinedTextField(
                            value = inputText,
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            onValueChange = {
                                inputText = it
                                val input = it.text.toIntOrNull()
                                if (input != null && input in 0..1000 && input % 50 == 0) {
                                    selectedIndex = countOptions.indexOf(input)
                                }
                            },
                            singleLine = true,
                            modifier = Modifier
                                .width(100.dp)
                                .padding(horizontal = 8.dp),
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 22.sp,
                                textAlign = TextAlign.Center
                            ),
                            placeholder = { Text("Enter") }
                        )
                    } else {
                        Text(
                            text = countOptions[selectedIndex].toString(),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .clickable { isEditing = true }
                        )
                    }

                    IconButton(
                        onClick = {
                            if (selectedIndex < countOptions.lastIndex) selectedIndex++
                            inputText = TextFieldValue(countOptions[selectedIndex].toString())
                        }
                    ) {
                        Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Increase")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onCountSelected(countOptions[selectedIndex])
                    onDismiss()
                }
            ) {
                Text("Set")
            }
        },
        dismissButton = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    selectedIndex = 0
                    inputText = TextFieldValue("0")
                }) {
                    Icon(Icons.Default.Delete, contentDescription = "Clear limit")
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    )
}
