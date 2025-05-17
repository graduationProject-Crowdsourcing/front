package project.graduation.crowd_sourcing.presentation.ui.screen.logout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import project.graduation.crowd_sourcing.presentation.ui.component.ConfirmButton
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen
import project.graduation.crowd_sourcing.presentation.ui.screen.alarm.AlarmView

@Composable
fun LogoutConfirmView(
    viewModel: LogoutConfirmViewModel = hiltViewModel(),
    navController: NavController
) {
    val context = LocalContext.current
    val logoutSuccess = viewModel.logoutSuccess

    if (logoutSuccess) {
        LaunchedEffect(Unit) {
            navController.navigate(Screen.LoginScreen.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        // 가운데 텍스트
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("로그아웃 하시겠습니까?", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        // 하단 버튼
        ConfirmButton(
            text = "로그아웃",
            onConfirm = { viewModel.logout() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}


@Composable
fun LogoutConfirmPreviewContent(onLogoutClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        // 상단 또는 중앙 메시지
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("로그아웃 하시겠습니까?", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        // 하단 고정 버튼
        ConfirmButton(
            text = "로그아웃",
            onConfirm = onLogoutClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LogoutConfirmPreview() {
    LogoutConfirmPreviewContent()
}
