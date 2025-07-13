package project.graduation.crowd_sourcing.presentation.ui.screen.request.request

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import project.graduation.crowd_sourcing.domain.model.entity.martsearch.MartEntity
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.ConfirmButton
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen
import project.graduation.crowd_sourcing.presentation.ui.screen.request.component.DateTimePickerDialog
import project.graduation.crowd_sourcing.presentation.ui.screen.request.component.DateTimeSelectorField
import project.graduation.crowd_sourcing.presentation.ui.screen.request.component.DistrictSearchField
import project.graduation.crowd_sourcing.presentation.ui.screen.request.component.InputTextField

// ──────────────────────────── 진입 함수 ────────────────────────────
@Composable
fun RequestFormView(
    navController: NavController,
    viewModel: RequestFormViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val requestState by viewModel.requestState.collectAsState()
    val categoryList by viewModel.categoryList.collectAsState()

    val selectedRegion = navController.currentBackStackEntry
        ?.savedStateHandle?.get<String>("selectedRegion")
    val selectedMarts = navController.currentBackStackEntry?.savedStateHandle?.get<List<MartEntity>>("selectedMarts")
    val prefillMartNames = navController.currentBackStackEntry?.savedStateHandle?.get<List<String>>("selectedMarts_prefill")

    LaunchedEffect(Unit) {
        selectedRegion?.let { viewModel.updateSelectedRegion(it) }
        selectedMarts?.let { viewModel.setMartList(it) } // Entity용
        prefillMartNames?.let { viewModel.updateSelectedMarts(it) } // 이름만
    }


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
        selectedRegion = viewModel.selectedRegion,
        selectedMarts = viewModel.selectedMarts,
        state = state,
        requestState = requestState,
        categoryList = categoryList,
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
    selectedRegion: String,
    selectedMarts: List<String>,
    state: RequestFormUiState,
    requestState: RequestState,
    categoryList: List<String>,
    onMaxPeopleChange: (String) -> Unit,
    onPointPerPersonChange: (String) -> Unit,
    onItemChange: (String) -> Unit,
    onExpirationDateChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onCategorySelected: (String) -> Unit
) {
    LocalContext.current

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
            selectedRegion = state.sigungu,
            iconResId = R.drawable.ic_mart,
            onClick = {
                navController.currentBackStackEntry?.savedStateHandle?.set(
                    "selectedRegion", selectedRegion)

                navController.currentBackStackEntry?.savedStateHandle?.set(
                    "selectedMarts_prefill", selectedMarts)

                navController.navigate(Screen.SelectRegionScreen.route)
            }
        )

        Spacer(modifier = Modifier.height(8.dp)) // 여백

        // 선택한 마트 리스트 표시
        if (selectedMarts.isNotEmpty()) {
            Text(
                text = "선택한 마트는 다음과 같습니다:\n${selectedMarts.joinToString(", ")}",
                fontSize = 14.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
            )
        }

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
                    showDatePicker = true
                }
        ) {
            DateTimeSelectorField(
                label = "기간 설정",
                dateTimeText = state.expirationDate,
                iconResId = calendarIconResId,
                onClick = {
                    showDatePicker = true
                }
            )
        }

        // 날짜 선택 다이얼로그 표시
        Log.d("RequestFormView", "showDatePicker 검사: $showDatePicker")
        if (showDatePicker) {
            DateTimePickerDialog(
                onDateTimeSelected = { dateTime ->
                    onExpirationDateChange(dateTime)
                    showDatePicker = false
                },
                onDismiss = {
                    showDatePicker = false
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