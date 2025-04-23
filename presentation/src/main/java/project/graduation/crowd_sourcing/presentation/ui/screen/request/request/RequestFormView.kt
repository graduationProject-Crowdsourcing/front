package project.graduation.crowd_sourcing.presentation.ui.screen.request.request

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.ConfirmButton
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen
import project.graduation.crowd_sourcing.presentation.ui.screen.request.component.*
import java.util.*

// ──────────────────────────── 진입 함수 ────────────────────────────
@Composable
fun RequestFormView(
    navController: NavController,
    viewModel: RequestFormViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    // 주요 입력 컴포넌트 호출
    RequestFormContent(
        state = state,
        viewModel = viewModel,
        onMartChange = viewModel::onMartChange,
        onMaxPeopleChange = viewModel::onMaxPeopleChange,
        onPointPerPersonChange = viewModel::onPointPerPersonChange,
        onItemChange = viewModel::onItemChange,
        onDateTimeChange = viewModel::onDateTimeChange,
        onSubmit = {
            navController.navigate(Screen.RequestCompleteScreen.route)
        }
    )
}

// ──────────────────────────── 본문 UI 구성 ────────────────────────────
@Composable
fun RequestFormContent(
    state: RequestFormUiState,
    viewModel: RequestFormViewModel,
    onMartChange: (String) -> Unit,
    onMaxPeopleChange: (String) -> Unit,
    onPointPerPersonChange: (String) -> Unit,
    onItemChange: (String) -> Unit,
    onDateTimeChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    // 날짜, 시간 상태 저장용
    val dateState = remember { mutableStateOf("") }
    val timeState = remember { mutableStateOf("") }

    // 날짜 선택 다이얼로그
    val datePickerDialog = remember(context) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                dateState.value = "%04d-%02d-%02d".format(year, month + 1, dayOfMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    // 시간 선택 다이얼로그
    val timePickerDialog = remember(context) {
        TimePickerDialog(
            context,
            { _, hour, minute ->
                timeState.value = "%02d:%02d".format(hour, minute)
                if (dateState.value.isNotBlank()) {
                    val combined = "${dateState.value} ${timeState.value}"
                    onDateTimeChange(combined)
                }
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
    }

    // 전체 폼 UI
    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        // 마트 선택 드롭다운
        MartDropdownField(
            label = "마트 선택",
            selectedMart = state.martName,
            martList = dummyMartList,
            onMartSelected = {
                viewModel.setSelectedMart(it)
            },
            iconResId = R.drawable.ic_mart
        )


        Spacer(modifier = Modifier.height(8.dp))

        // 인원 수 입력
        InputTextField(
            label = "의뢰 수행 인원 수",
            value = state.maxPeople,
            onValueChange = onMaxPeopleChange,
            placeholder = "최대 인원 수",
            iconResId = R.drawable.ic_person,
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 포인트 입력
        InputTextField(
            label = "지급 포인트 설정",
            value = state.pointPerPerson,
            onValueChange = onPointPerPersonChange,
            placeholder = "작업 당 지급 포인트",
            iconResId = R.drawable.ic_point,
            keyboardType = KeyboardType.Number
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 품목 입력
        InputTextField(
            label = "품목 작성",
            value = state.item,
            onValueChange = onItemChange,
            placeholder = "요청할 품목 작성",
            iconResId = R.drawable.ic_item,
            keyboardType = KeyboardType.Text
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 날짜/시간 선택 필드
        DateTimeSelectorField(
            label = "기간 설정",
            dateTimeText = state.dateTime,
            iconResId = R.drawable.ic_calendar,
            onClick = {
                datePickerDialog.show()
                datePickerDialog.setOnDismissListener {
                    timePickerDialog.show()
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 마트 선택 시 지도 표시
        if (state.martLat != null && state.martLng != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                MartLocationMapView(
                    latitude = state.martLat,
                    longitude = state.martLng,
                    title = state.martName
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // 의뢰 신청 버튼
        ConfirmButton(
            text = "의뢰 신청하기",
            onConfirm = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
    }
}

// ──────────────────────────── 프리뷰 ────────────────────────────
@Preview(showBackground = true)
@Composable
fun RequestFormContentPreview() {
    val fakeViewModel = object : RequestFormViewModel() {}

    RequestFormContent(
        state = RequestFormUiState(
            martName = "상암 홈플러스",
            maxPeople = "10",
            pointPerPerson = "100",
            item = "서울우유 500ML",
            dateTime = "2025-04-12 14:00"
        ),
        viewModel = fakeViewModel,
        onMartChange = {},
        onMaxPeopleChange = {},
        onPointPerPersonChange = {},
        onItemChange = {},
        onDateTimeChange = {},
        onSubmit = {}
    )
}
