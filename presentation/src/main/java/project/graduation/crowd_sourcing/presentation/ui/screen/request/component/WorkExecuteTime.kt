package project.graduation.crowd_sourcing.presentation.ui.screen.request.component

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.util.*

@Composable
fun WorkExecuteTime(
    initialText: String,
    onDateTimeChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }

    // 시간 선택 후 두 값이 다 있으면 콜백 호출
    fun emitDateTime() {
        if (selectedDate.isNotBlank() && selectedTime.isNotBlank()) {
            onDateTimeChange("$selectedDate $selectedTime")
        }
    }

    val timePickerDialog = remember {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                selectedTime = "%02d:%02d".format(hour, minute)
                emitDateTime()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
    }

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                selectedDate = "%04d-%02d-%02d".format(year, month + 1, day)
                timePickerDialog.show() // 날짜 고른 후 → 시간 선택
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Text(
        text = if (initialText.isNotBlank()) initialText else "수행 날짜/시간 선택",
        modifier = modifier
            .clickable { datePickerDialog.show() }
            .padding(4.dp),
        style = MaterialTheme.typography.bodyMedium,
        color = if (initialText.isNotBlank()) Color.Black else Color.Gray
    )
}
