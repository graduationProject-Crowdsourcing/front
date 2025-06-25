package project.graduation.crowd_sourcing.presentation.ui.screen.request.request

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.ConfirmButton
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen
import project.graduation.crowd_sourcing.presentation.ui.screen.request.component.*
import androidx.compose.runtime.saveable.rememberSaveable
import android.util.Log
import androidx.compose.foundation.clickable

// ──────────────────────────── 진입 함수 ────────────────────────────
@Composable
fun RequestFormView(
    navController: NavController,
    viewModel: RequestFormViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val requestState by viewModel.requestState.collectAsState()
    val districtSuggestions by viewModel.districtSuggestions.collectAsState()

    // 요청 상태 처리
    LaunchedEffect(requestState) {
        when (requestState) {
            is RequestState.Success -> {
                // 요청 성공 시 완료 화면으로 이동
                navController.navigate(Screen.RequestCompleteScreen.route) {
                    // 현재 화면은 백스택에서 제거
                    popUpTo(Screen.RequestFormScreen.route) { inclusive = true }
                }
                // 요청 상태 초기화
                viewModel.resetRequestState()
            }
            else -> {} // 다른 상태는 UI에서 처리
        }
    }

    // 주요 입력 컴포넌트 호출
    RequestFormContent(
        state = state,
        requestState = requestState,
        districtSuggestions = districtSuggestions,
        onSigunguChange = viewModel::onSigunguChange,
        onDistrictSelected = viewModel::onDistrictSelected,
        onMaxPeopleChange = viewModel::onMaxPeopleChange,
        onPointPerPersonChange = viewModel::onPointPerPersonChange,
        onItemChange = viewModel::onItemChange,
        onExpirationDateChange = viewModel::onExpirationDateChange,
        onSubmit = viewModel::submitRequest
    )
}

// ──────────────────────────── 본문 UI 구성 ────────────────────────────
@Composable
fun RequestFormContent(
    state: RequestFormUiState,
    requestState: RequestState,
    districtSuggestions: List<String>,
    onSigunguChange: (String) -> Unit,
    onDistrictSelected: (String) -> Unit,
    onMaxPeopleChange: (String) -> Unit,
    onPointPerPersonChange: (String) -> Unit,
    onItemChange: (String) -> Unit,
    onExpirationDateChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val context = LocalContext.current
    
    // 날짜 선택 다이얼로그 표시 상태 (rememberSaveable로 변경)
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    
    // 달력 아이콘 리소스 안전하게 가져오기
    val calendarIconResId = try {
        R.drawable.ic_calendar
    } catch (e: Exception) {
        // 달력 아이콘을 사용할 수 없는 경우, 다른 아이콘으로 대체
        R.drawable.ic_item // 또는 다른 존재하는 아이콘
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 상태에 따른 메시지 표시
        when (requestState) {
            is RequestState.Error -> {
                Text(
                    text = requestState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            is RequestState.Loading -> {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }
            else -> {}
        }

        // 지역 검색 및 선택
        DistrictSearchField(
            query = state.sigungu,
            suggestions = districtSuggestions,
            onQueryChange = onSigunguChange,
            onSuggestionClick = onDistrictSelected,
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    Log.d("RequestFormView", "기간 설정 Box 클릭됨")
                    showDatePicker = true
                }
        ) {
            DateTimeSelectorField(
                label = "기간 설정",
                dateTimeText = state.expirationDate,
                iconResId = calendarIconResId,
                onClick = {
                    Log.d("RequestFormView", "기간 설정 필드 클릭됨, 현재 상태: showDatePicker=$showDatePicker")
                    showDatePicker = true
                    Log.d("RequestFormView", "showDatePicker 상태 변경됨: $showDatePicker")
                }
            )
        }
        
        // 날짜 선택 다이얼로그 표시
        Log.d("RequestFormView", "showDatePicker 검사: $showDatePicker")
        if (showDatePicker) {
            Log.d("RequestFormView", "DateTimePickerDialog 표시 조건 충족됨")
            DateTimePickerDialog(
                onDateTimeSelected = { dateTime ->
                    Log.d("RequestFormView", "날짜/시간 선택됨: $dateTime")
                    onExpirationDateChange(dateTime)
                    showDatePicker = false
                    Log.d("RequestFormView", "showDatePicker 상태 false로 변경됨")
                },
                onDismiss = { 
                    Log.d("RequestFormView", "날짜/시간 선택 취소됨")
                    showDatePicker = false
                    Log.d("RequestFormView", "showDatePicker 상태 false로 변경됨")
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 의뢰 신청 버튼
        ConfirmButton(
            text = "의뢰 신청하기",
            onConfirm = onSubmit,
            enabled = requestState !is RequestState.Loading,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
    }
}