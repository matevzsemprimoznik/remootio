package com.techcube.remootio.ui.screens
import TimePicker
import TimePickerState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showBackground = true)
@Composable
fun SettingsScreen() {
    val selectedTime = remember { mutableStateOf(TimePickerState(22, 30)) }

    LaunchedEffect(selectedTime.value) {
        println("Selected Time Changed: ${selectedTime.value.hour}:${selectedTime.value.minute}")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
    ) {
        Text(
            text = "Automatic Closing Time",
            modifier = Modifier.padding(bottom = 4.dp),
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Normal)
        )

        TimePicker(selectedTime = selectedTime)
    }

}