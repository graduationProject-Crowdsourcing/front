package project.graduation.crowd_sourcing.presentation.ui.screen.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import project.graduation.crowd_sourcing.presentation.R

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
            .padding(24.dp),
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

        OutlinedTextField(
            value = state.email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        state.errorMessage?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onLoginClick,
            enabled = state.isLoginEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("로그인")
        }

        if (!isSignUpCompleted) {
            OutlinedButton(
                onClick = onSignUpClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("회원가입")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(viewModel: LoginViewModel = hiltViewModel()) {
    val state = viewModel.uiState
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

    if (isSignUpSheetVisible) {
        ModalBottomSheet(onDismissRequest = { isSignUpSheetVisible = false }) {
            SignUpView(
                onSignUpSuccess = {
                    viewModel.onSignUpSuccess()
                    Toast.makeText(context, "회원가입 완료!", Toast.LENGTH_SHORT).show()
                    isSignUpSheetVisible = false
                }
            )
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

