package project.graduation.crowd_sourcing.presentation.ui.screen.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.CancelButton
import project.graduation.crowd_sourcing.presentation.ui.component.ConfirmButton
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen
import project.graduation.crowd_sourcing.presentation.ui.component.EditTextBox

@Composable
fun LoginScreenContent(
    state: LoginUiState,
    isSignUpCompleted: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.space_medium)),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "PRICE-IT",
            fontSize = 55.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.primary),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(100.dp))

        Text(text = "ID", fontWeight = FontWeight.Bold)
        EditTextBox(
            value = state.email,
            onValueChange = onEmailChange,
            placeHolder = "ID"
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_medium)))

        Text(text = "Password", fontWeight = FontWeight.Bold)
        EditTextBox(
            value = state.password,
            onValueChange = onPasswordChange,
            placeHolder = "Password"
        )

        state.errorMessage?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(30.dp))

        ConfirmButton(
            text = "로그인",
            onConfirm = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_small)))

        if (!isSignUpCompleted) {
            CancelButton(
                text = "회원가입",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                textColor = colorResource(id = R.color.primary),
                borderColor = colorResource(id = R.color.primary),
                onConfirm = onSignUpClick
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(navController: NavHostController) {
    val viewModel: LoginViewModel = hiltViewModel()
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var isSignUpSheetVisible by remember { mutableStateOf(false) }
    val isSignUpCompleted = viewModel.isSignUpCompleted

    LoginScreenContent(
        state = state,
        isSignUpCompleted = isSignUpCompleted,
        onEmailChange = viewModel::onEmailChanged,
        onPasswordChange = viewModel::onPasswordChanged,
        onLoginClick = viewModel::onLoginClick,
        onSignUpClick = { isSignUpSheetVisible = true }
    )

    // 로그인 성공 시 홈으로 이동
    LaunchedEffect(viewModel.isLoginSuccess) {
        if (viewModel.isLoginSuccess) {
            navController.navigate(Screen.BottomScreen.HomeScreen.bRoute) {
                popUpTo(Screen.LoginScreen.route) { inclusive = true }
            }
        }
    }

    // 회원가입 바텀시트
    if (isSignUpSheetVisible) {
        ModalBottomSheet(onDismissRequest = { isSignUpSheetVisible = false }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()) // 스크롤 가능
                    .imePadding()                          // 키보드 안 가림
                    .padding(16.dp)
            ) {
                SignUpView(
                    viewModel = viewModel,
                    onSignUpSuccess = {
                        viewModel.onSignUpSuccess()
                        Toast.makeText(context, "회원가입 완료!", Toast.LENGTH_SHORT).show()
                        isSignUpSheetVisible = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreenContent(
        state = LoginUiState.init(),
        isSignUpCompleted = false,
        onEmailChange = {},
        onPasswordChange = {},
        onLoginClick = {},
        onSignUpClick = {}
    )
}
