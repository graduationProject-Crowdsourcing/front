package project.graduation.crowd_sourcing.presentation.ui.screen.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignUpView(onSignUpSuccess: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .widthIn(max = 400.dp)
    ) {
        Text("회원가입", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // 1. 아이디
        LabeledInputFieldWithButton(label = "아이디")

        // 2. 비밀번호
        LabeledInputField(label = "비밀번호")

        // 3. 비밀번호 확인
        LabeledInputFieldWithButton(label = "비밀번호 확인")

        // 4. 닉네임
        LabeledInputFieldWithButton(label = "닉네임")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // TODO: 실제 회원가입 로직
                onSignUpSuccess()
            },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text("회원가입")
        }
    }
}

@Composable
fun LabeledInputFieldWithButton(label: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .widthIn(max = 400.dp)
    ) {
        // 입력 필드 + 확인 버튼 (Row로 배치)
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            androidx.compose.material3.OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(label) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(4.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { /* TODO: 확인 로직 */ }) {
                Text("확인")
            }
        }
        //Text("error message / ok message", fontSize = 12.sp, color = androidx.compose.ui.graphics.Color.Red)
    }

    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun LabeledInputField(label: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .widthIn(max = 400.dp)
    ) {
        androidx.compose.material3.OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
        //Text("error message / ok message", fontSize = 12.sp, color = androidx.compose.ui.graphics.Color.Red)
    }

    Spacer(modifier = Modifier.height(12.dp))
}

@Preview(showBackground = true)
@Composable
fun SignUpViewPreview() {
    SignUpView(onSignUpSuccess = {})
}

