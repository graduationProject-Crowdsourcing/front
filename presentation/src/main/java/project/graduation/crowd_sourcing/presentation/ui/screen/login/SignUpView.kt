package project.graduation.crowd_sourcing.presentation.ui.screen.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .widthIn(max = 400.dp)
    ) {
        Text("회원가입", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Text("아이디", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp))
        EditTextBox(
            value = username,
            onValueChange = { username = it },
            placeHolder = "아이디",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("비밀번호", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp))
        EditTextBox(
            value = password,
            onValueChange = { password = it },
            placeHolder = "비밀번호",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("비밀번호 확인", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp))
        EditTextBox(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeHolder = "비밀번호 확인",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("닉네임", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 10.dp))
        EditTextBox(
            value = nickname,
            onValueChange = { nickname = it },
            placeHolder = "닉네임",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ConfirmButton(
            text = "회원가입",
            onConfirm = {
                if (password == confirmPassword) {
                    viewModel.signUp(username, password, nickname)
                    onSignUpSuccess()
                } else {
                    // 비밀번호 불일치 알림 (선택사항)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

//@Composable
//fun InputWithButton(label: String) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 10.dp)
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            EditTextBox(
//                value = "",
//                onValueChange = {},
//                placeHolder = label,
//                modifier = Modifier.weight(1f)
//            )
//
//            Spacer(modifier = Modifier.width(8.dp))
//
//            ConfirmButton(
//                text = "확인",
//                onConfirm = { /* TODO */ },
//                modifier = Modifier
//                    .align(Alignment.CenterVertically)
//            )
//        }
//
//        Spacer(modifier = Modifier.height(12.dp))
//    }
//}

//@Preview(showBackground = true, widthDp = 400, heightDp = 800)
//@Composable
//fun SignUpViewPreview() {
//    SignUpView(onSignUpSuccess = {})
//}
