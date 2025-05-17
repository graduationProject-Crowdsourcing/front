package project.graduation.crowd_sourcing.presentation.ui.screen.request.work

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.GrayDivider
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen
import project.graduation.crowd_sourcing.presentation.ui.screen.request.component.WorkExecuteTime
import project.graduation.crowd_sourcing.presentation.ui.screen.request.component.WorkInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubmitWorkView(
    navController: NavController,
    workId: String? = null,
    viewModel: SubmitWorkViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // 이미지 업로드 런처
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.updateImage(it)

            // 선택 즉시 업로드 실행
            viewModel.uploadImage(
                context = context,
                username = "worker123",          // TODO: 실제 ID
                commissionId = uiState.workId    // 작업 ID
            )
        }
    }

    // workId가 바뀔 때마다 데이터 로드
    LaunchedEffect(workId) {
        workId?.let { viewModel.loadWorkInfo(it) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 상품 이미지 업로드
        Text(text = "상품 이미지", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray)
                .clickable { launcher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (uiState.imageUri != null) {
                Text(
                    text = "이미지 업로드 완료",
                    color = Color.Black,
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Text(
                    text = "클릭하여 사진을 업로드하세요",
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 작업 내용
        Text(text = "작업 내용", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(12.dp))

        // 위치, 의뢰 내역
        WorkInfo(Icons.Default.Place, "위치", uiState.place)
        Spacer(modifier = Modifier.height(12.dp))
        GrayDivider()
        Spacer(modifier = Modifier.height(12.dp))

        WorkInfo(Icons.Default.Edit, "의뢰 내역", uiState.title)
        Spacer(modifier = Modifier.height(12.dp))
        GrayDivider()

        // 가격 입력
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AttachMoney,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "가격",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.width(80.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            TextField(
                value = uiState.price,
                onValueChange = { viewModel.updatePrice(it) },
                singleLine = true,
                modifier = Modifier
                    .width(200.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent, // 배경 없음
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary, // 포커스 시 밑줄 색
                    unfocusedIndicatorColor = Color.Gray, // 기본 밑줄 색
                    disabledIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                trailingIcon = {
                    Text("원", style = MaterialTheme.typography.bodyMedium)
                }
            )
        }
        GrayDivider()
        Spacer(modifier = Modifier.height(12.dp))

        // 수행 시간 입력
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "수행 시간",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.width(80.dp),
            )

            WorkExecuteTime(
                initialText = uiState.executeTime,
                onDateTimeChange = { viewModel.updateExecuteTime(it) },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        GrayDivider()
        Spacer(modifier = Modifier.height(12.dp))

        // 위치 인증
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "위치 인증",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.width(80.dp)
            )

            Text(
                text = if (uiState.locationVerified) "✅ 인증되었습니다" else "여기를 클릭해주세요",
                color = if (uiState.locationVerified)
                    MaterialTheme.colorScheme.primary
                else
                    Color.Gray,
                modifier = Modifier
                    .weight(1f)
                    .clickable { viewModel.verifyLocation() },
                style = MaterialTheme.typography.bodyMedium
            )
        }


        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                navController.navigate(
                    Screen.WorkCompleteScreen.createRoute(
                        uiState.place,
                        uiState.title,
                        uiState.reward
                    )
                ) {
                    popUpTo(Screen.SubmitWorkScreen.route) { inclusive = true }
                }
            },
            enabled = viewModel.isSavable(), // 저장 조건 만족 시 활성화
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.primary),
                disabledContainerColor = Color.LightGray
            ),
            shape = RoundedCornerShape(dimensionResource(R.dimen.round_common))
        ) {
            Text(
                text = "저장",
                color = Color.White
            )
        }
    }
}
