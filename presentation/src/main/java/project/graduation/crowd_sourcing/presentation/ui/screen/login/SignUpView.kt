package project.graduation.crowd_sourcing.presentation.ui.screen.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.graduation.crowd_sourcing.presentation.ui.component.ConfirmButton
import project.graduation.crowd_sourcing.presentation.ui.component.EditTextBox

@Composable
fun SignUpView(
    viewModel: LoginViewModel,
    onSignUpSuccess: () -> Unit
) {
    val isSignUpSuccess by viewModel.isSignUpSuccess.collectAsState()

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }

    // Focus 요청자들 선언
    val passwordFocusRequester = remember { FocusRequester() }
    val confirmPasswordFocusRequester = remember { FocusRequester() }
    val nicknameFocusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(isSignUpSuccess) {
        if (isSignUpSuccess) {
            onSignUpSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .widthIn(max = 400.dp)
    ) {
        Text("회원가입", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // 아이디
        Text("아이디", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp))
        EditTextBox(
            value = username,
            onValueChange = { username = it },
            placeHolder = "아이디",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { passwordFocusRequester.requestFocus() }
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 비밀번호
        Text("비밀번호", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp))
        EditTextBox(
            value = password,
            onValueChange = { password = it },
            placeHolder = "비밀번호",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .focusRequester(passwordFocusRequester),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { confirmPasswordFocusRequester.requestFocus() }
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 비밀번호 확인
        Text("비밀번호 확인", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp))
        EditTextBox(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeHolder = "비밀번호 확인",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .focusRequester(confirmPasswordFocusRequester),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { nicknameFocusRequester.requestFocus() }
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 닉네임
        Text("닉네임", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp))
        EditTextBox(
            value = nickname,
            onValueChange = { nickname = it },
            placeHolder = "닉네임",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .focusRequester(nicknameFocusRequester),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        ConfirmButton(
            text = "회원가입",
            onConfirm = {
                if (password == confirmPassword) {
                    viewModel.signUp(username, password, nickname)
                } else {
                    // 비밀번호 불일치 처리
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

