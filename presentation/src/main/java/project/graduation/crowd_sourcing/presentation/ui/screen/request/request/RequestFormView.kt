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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp

// ──────────────────────────── 진입 함수 ────────────────────────────
@Composable
fun RequestFormView(
    navController: NavController,
    viewModel: RequestFormViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val requestState by viewModel.requestState.collectAsState()
    val districtSuggestions by viewModel.districtSuggestions.collectAsState()
    val categoryList by viewModel.categoryList.collectAsState()


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
        navController = navController,
        selectedRegions = viewModel.selectedRegions,
        state = state,
        requestState = requestState,
        districtSuggestions = districtSuggestions,
        categoryList = categoryList,
        onSigunguChange = viewModel::onSigunguChange,
        onDistrictSelected = viewModel::onDistrictSelected,
        onMaxPeopleChange = viewModel::onMaxPeopleChange,
        onPointPerPersonChange = viewModel::onPointPerPersonChange,
        onItemChange = viewModel::onItemChange,
        onExpirationDateChange = viewModel::onExpirationDateChange,
        onSubmit = viewModel::submitRequest,
        onCategorySelected = viewModel::onCategorySelected
    )
}

// ──────────────────────────── 본문 UI 구성 ────────────────────────────
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestFormContent(
    navController: NavController,
    selectedRegions: List<String>,
    state: RequestFormUiState,
    requestState: RequestState,
    districtSuggestions: List<String>,
    categoryList: List<String>,
    onSigunguChange: (String) -> Unit,
    onDistrictSelected: (String) -> Unit,
    onMaxPeopleChange: (String) -> Unit,
    onPointPerPersonChange: (String) -> Unit,
    onItemChange: (String) -> Unit,
    onExpirationDateChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onCategorySelected: (String) -> Unit
) {
    val context = LocalContext.current

    // 선택된 카테고리 관리
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by rememberSaveable { mutableStateOf("") }

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
            selectedRegions = selectedRegions,
            iconResId = R.drawable.ic_mart,
            onClick = {
                navController.navigate(Screen.SelectRegionScreen.route)
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        val viewModel: RequestFormViewModel = hiltViewModel()
        val martList by viewModel.martList.collectAsState()

        // 마트 리스트가 있을 때만 표시
        if (martList.isNotEmpty()) {
            Text(
                text = "선택하신 지역의 마트 목록입니다:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            // 스크롤 가능한 고정 높이 박스
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp) // 높이 조정 가능
                    .padding(bottom = 8.dp)
            ) {
                // 이 영역 내부만 스크롤
                LazyColumn {
                    items(martList) { mart ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = mart.martName, style = MaterialTheme.typography.bodyLarge)
                                mart.sigungu?.let {
                                    Text(text = it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            }
        }

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

        // 카테고리 선택창
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            InputTextField(
                label = "카테고리 선택",
                value = selectedCategory,
                onValueChange = {}, // 사용자 직접 입력 X
                placeholder = "카테고리를 선택하세요",
                iconResId = R.drawable.ic_item,
                modifier = Modifier
                    .menuAnchor() // 드롭다운 위치 고정용
                    .fillMaxWidth(),
                customContent = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = colorResource(id = R.color.gray),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(color = colorResource(id = R.color.white))
                            .clickable { expanded = true }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = if (selectedCategory.isNotBlank()) selectedCategory else "카테고리를 선택하세요",
                            color = if (selectedCategory.isNotBlank()) Color.Black else Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categoryList.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(text = category) },
                        onClick = {
                            selectedCategory = category
                            onCategorySelected(category)
                            expanded = false
                        }
                    )
                }
            }
        }


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