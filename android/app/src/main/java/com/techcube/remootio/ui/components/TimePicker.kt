import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class TimePickerState(
    val hour: Int,
    val minute: Int
)

@Composable
fun TimePicker(
    selectedTime: MutableState<TimePickerState>,
) {
    val hours = (0..23).map { it.toString().padStart(2, '0') }
    val minutes = (0..59).map { it.toString().padStart(2, '0') }

    val height = 56.dp

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            modifier = Modifier.height(height)
        ) {
            LazyColumnWithSnap(items = hours,
                viewportHeightDp = height,
                itemHeightDp = 48.dp,
                itemWidthDp = 44.dp,
                initialItem = selectedTime.value.hour,
                onChange = { selectedItem ->
                    selectedTime.value = TimePickerState(
                        hour = selectedItem.toInt(),
                        minute = selectedTime.value.minute
                    )
                },
                columnItem = { item ->
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = item,
                            style = TextStyle(fontSize = 34.sp, fontWeight = FontWeight.Normal)
                        )
                    }
                }
            )


            Box(
                modifier = Modifier.padding(bottom = 6.dp).height(height)
            ) {
                Text(
                    text = ":",
                    style = TextStyle(
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Normal,
                    ),
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }

            LazyColumnWithSnap(
                items = minutes,
                viewportHeightDp = height,
                itemHeightDp = 48.dp,
                itemWidthDp = 44.dp,
                initialItem = selectedTime.value.minute,
                onChange = { selectedItem ->
                    selectedTime.value = TimePickerState(
                        hour = selectedTime.value.hour,
                        minute = selectedItem.toInt()
                    )
                },
                columnItem = { item ->
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center,) {
                        Text(
                            text = item,
                            style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Normal)
                        )
                    }
                }
            )
        }
    }
}
