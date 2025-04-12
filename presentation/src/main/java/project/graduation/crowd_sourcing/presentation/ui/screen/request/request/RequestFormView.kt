package project.graduation.crowd_sourcing.presentation.ui.screen.request.request

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.viewmodel.request.RequestFormViewModel
import project.graduation.crowd_sourcing.presentation.ui.component.ConfirmButton
import project.graduation.crowd_sourcing.presentation.ui.screen.request.component.InputTextField
import project.graduation.crowd_sourcing.presentation.ui.screen.request.component.MartDropdownField
import project.graduation.crowd_sourcing.presentation.ui.screen.request.component.DateTimeSelectorField
import java.util.*

@Composable
fun RequestFormView(
    navController: NavController,
    viewModel: RequestFormViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    RequestFormContent(
        state = state,
        onMartChange = viewModel::onMartChange,
        onMaxPeopleChange = viewModel::onMaxPeopleChange,
        onPointPerPersonChange = viewModel::onPointPerPersonChange,
        onItemChange = viewModel::onItemChange,
        onDateTimeChange = viewModel::onDateTimeChange,
        onSubmit = {
            navController.navigate("request_complete")
        }
    )
}

@Composable
fun RequestFormContent(
    state: RequestFormUiState,
    onMartChange: (String) -> Unit,
    onMaxPeopleChange: (String) -> Unit,
    onPointPerPersonChange: (String) -> Unit,
    onItemChange: (String) -> Unit,
    onDateTimeChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val dateState = remember { mutableStateOf("") }
    val timeState = remember { mutableStateOf("") }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            dateState.value = "%04d-%02d-%02d".format(year, month + 1, dayOfMonth)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
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

    Column(Modifier.padding(16.dp)) {
        val martList = listOf("상암 홈플러스", "마포 이마트", "은평 롯데마트")

        MartDropdownField(
            label = "마트 선택",
            selectedMart = state.martName,
            martList = martList,
            onMartSelected = onMartChange,
            iconResId = R.drawable.ic_mart
        )

        Spacer(modifier = Modifier.height(8.dp))

        InputTextField(
            label = "의뢰 수행 인원 수",
            value = state.maxPeople,
            onValueChange = onMaxPeopleChange,
            placeholder = "최대 인원 수",
            iconResId = R.drawable.ic_person
        )

        Spacer(modifier = Modifier.height(8.dp))

        InputTextField(
            label = "지급 포인트 설정",
            value = state.pointPerPerson,
            onValueChange = onPointPerPersonChange,
            placeholder = "작업 당 지급 포인트",
            iconResId = R.drawable.ic_point
        )

        Spacer(modifier = Modifier.height(8.dp))

        InputTextField(
            label = "품목 선택",
            value = state.item,
            onValueChange = onItemChange,
            placeholder = "품목",
            iconResId = R.drawable.ic_item
        )

        Spacer(modifier = Modifier.height(8.dp))

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

        Image(
            painter = painterResource(id = android.R.drawable.ic_menu_mapmode),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ConfirmButton(
            text = "의뢰 신청하기",
            onConfirm = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RequestFormContentPreview() {
    RequestFormContent(
        state = RequestFormUiState(
            martName = "상암 홈플러스",
            maxPeople = "10",
            pointPerPerson = "100",
            item = "서울우유 500ML",
            dateTime = "2025-04-12 14:00"
        ),
        onMartChange = {},
        onMaxPeopleChange = {},
        onPointPerPersonChange = {},
        onItemChange = {},
        onDateTimeChange = {},
        onSubmit = {}
    )
}
