package project.graduation.crowd_sourcing.presentation.ui.screen.request.component

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import project.graduation.crowd_sourcing.presentation.ui.component.EditTextBox
import java.util.*
import android.util.Log
import android.widget.Toast

// 의뢰 작성 페이지 - 기간 설정 (의뢰 마감 날짜, 시간 선택) 컴포넌트
@Composable
fun DateTimeSelectorField(
    label: String,
    dateTimeText: String,
    onClick: () -> Unit,
    @DrawableRes iconResId: Int
) {
    Log.d("DateTimeSelectorField", "컴포넌트 렌더링 됨, 현재 텍스트: $dateTimeText")

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                Log.d("DateTimeSelectorField", "Row 영역 클릭됨")
                onClick()
            }
    ) {
        // 아이콘
        Box(modifier = Modifier.width(30.dp)) {
            // 아이콘
            Icon(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier.padding(top = 25.dp, end = 15.dp)
            )
        }

        // 라벨 + 날짜/시간 필드
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // Box로 감싸서 전체 필드 클릭 처리
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { 
                        Log.d("DateTimeSelectorField", "Box 영역 클릭됨")
                        onClick() 
                    }
            ) {
                EditTextBox(
                    value = dateTimeText,
                    onValueChange = {},
                    placeHolder = "의뢰 마감 날짜 및 시간 선택",
                    readOnly = true,
                    enabled = false,
                    onClick = {
                        Log.d("DateTimeSelectorField", "EditTextBox 클릭됨")
                        onClick()
                    }
                )
            }
        }
    }
}

// 개선된 날짜 시간 선택기 - Material Dialog를 사용
@Composable
fun DateTimePickerDialog(
    onDateTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    
    // 현재 날짜/시간 정보
    val currentYear = calendar.get(Calendar.YEAR)
    val currentMonth = calendar.get(Calendar.MONTH)
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)
    
    // 디버그 로그 추가
    Log.d("DateTimePickerDialog", "현재 시간: ${calendar.time}")
    
    // rememberSaveable을 사용하여 화면 회전 시에도 상태 유지
    val showTimePicker = rememberSaveable { mutableStateOf(false) }
    val selectedDate = rememberSaveable { mutableStateOf("") }
    val selectedYear = rememberSaveable { mutableStateOf(currentYear) }
    val selectedMonth = rememberSaveable { mutableStateOf(currentMonth) }
    val selectedDay = rememberSaveable { mutableStateOf(currentDay) }
    
    // 날짜 선택 다이얼로그 생성
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                selectedYear.value = year
                selectedMonth.value = month
                selectedDay.value = dayOfMonth
                selectedDate.value = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                
                Log.d("DateTimePickerDialog", "선택된 날짜: ${selectedDate.value}")
                
                // 오늘 날짜를 선택한 경우에만 현재 시간 이후로 제한
                val isToday = (year == currentYear && month == currentMonth && dayOfMonth == currentDay)
                
                showTimePicker.value = true
            },
            currentYear,
            currentMonth,
            currentDay
        ).apply {
            // 최소 날짜를 오늘로 설정
            datePicker.minDate = calendar.timeInMillis
            setOnCancelListener { onDismiss() }
        }
    }
    
    // 컴포넌트가 처음 표시될 때만 다이얼로그 표시 (key를 Unit 대신 true로 변경)
    LaunchedEffect(true) {
        try {
            datePickerDialog.show()
        } catch (e: Exception) {
            // 예외 처리 추가
            Log.e("DateTimePickerDialog", "날짜 선택기 표시 중 오류", e)
            onDismiss()
        }
    }
    
    // 시간 선택 다이얼로그
    if (showTimePicker.value) {
        // 오늘 날짜 선택 여부 확인
        val isToday = (selectedYear.value == currentYear && 
                      selectedMonth.value == currentMonth && 
                      selectedDay.value == currentDay)
        
        // 시간 재선택이 필요한지 확인하는 상태
        val needTimeReselect = remember { mutableStateOf(false) }
        
        // 시간 선택 콜백 함수 정의
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            // 오늘 날짜이고 선택한 시간이 현재 시간보다 이전인지 확인
            if (isToday && (hourOfDay < currentHour || (hourOfDay == currentHour && minute <= currentMinute))) {
                // 현재 시간보다 이전 시간을 선택한 경우
                Toast.makeText(context, "현재 시간 이후로 선택해주세요", Toast.LENGTH_SHORT).show()
                
                // 다시 시간을 선택해야 함을 표시
                needTimeReselect.value = true
            } else {
                // 유효한 시간 선택
                val selectedDateTime = String.format(
                    "%s %02d:%02d",
                    selectedDate.value, hourOfDay, minute
                )
                
                Log.d("DateTimePickerDialog", "최종 선택된 날짜/시간: $selectedDateTime")
                
                onDateTimeSelected(selectedDateTime)
                onDismiss()
            }
        }
        
        // 시간 선택 다이얼로그 생성
        val timePickerDialog = TimePickerDialog(
            context,
            timeSetListener,
            if (isToday) currentHour + 1 else 9, // 오늘이면 현재 시간 + 1시간, 아니면 오전 9시로 기본값 설정
            if (isToday) currentMinute else 0,
            true
        )
        
        // 시간 재선택이 필요한 경우, 새 다이얼로그 생성
        LaunchedEffect(needTimeReselect.value) {
            if (needTimeReselect.value) {
                // 임시 flag 초기화
                needTimeReselect.value = false
                
                // 새 시간 선택 다이얼로그 생성 및 표시
                val newTimePickerDialog = TimePickerDialog(
                    context,
                    timeSetListener,
                    if (isToday) currentHour + 1 else 9,
                    if (isToday) currentMinute else 0,
                    true
                )
                
                try {
                    // 이전 다이얼로그가 열려있다면 닫기
                    if (timePickerDialog.isShowing) {
                        timePickerDialog.dismiss()
                    }
                    
                    // 새 다이얼로그 표시
                    newTimePickerDialog.show()
                } catch (e: Exception) {
                    Log.e("DateTimePickerDialog", "시간 재선택 다이얼로그 표시 중 오류", e)
                }
            }
        }
        
        // 시간 피커 표시 (showTimePicker의 변경을 감지하여 표시)
        LaunchedEffect(showTimePicker.value) {
            try {
                timePickerDialog.show()
                timePickerDialog.setOnCancelListener {
                    showTimePicker.value = false
                    datePickerDialog.show()
                }
            } catch (e: Exception) {
                // 예외 처리 추가
                Log.e("DateTimePickerDialog", "시간 선택기 표시 중 오류", e)
                showTimePicker.value = false
                onDismiss()
            }
        }
    }
    
    // 컴포넌트가 화면에서 제거될 때 다이얼로그도 제거
    DisposableEffect(true) {
        onDispose {
            try {
                if (datePickerDialog.isShowing) {
                    datePickerDialog.dismiss()
                }
            } catch (e: Exception) {
                // 예외 무시
            }
        }
    }
}
