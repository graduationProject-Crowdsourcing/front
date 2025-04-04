package project.graduation.crowd_sourcing.presentation.ui.screen.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.ConfirmButton
import project.graduation.crowd_sourcing.presentation.ui.screen.login.component.EditTextBox

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
        InputWithButton(label = "아이디")

        // 2. 비밀번호
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .widthIn(max = 400.dp)
        ) {
            Text(text = "비밀번호", fontWeight = FontWeight.Bold)

            EditTextBox(
                value = "",
                onValueChange = {},
                label = "비밀번호"
            )
        }
        Spacer(modifier = Modifier.height(12.dp)) // 입력칸 간격

        // 3. 비밀번호 확인
        InputWithButton(label = "비밀번호 확인")

        // 4. 닉네임
        InputWithButton(label = "닉네임")

        Spacer(modifier = Modifier.height(16.dp))

        ConfirmButton(
            text = "회원가입",
            onConfirm = onSignUpSuccess,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        )
    }
}

@Composable
fun InputWithButton(label: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .widthIn(max = 400.dp)
    ) {
        Text(text = label, fontWeight = FontWeight.Bold)

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            EditTextBox(
                value = "",
                onValueChange = {},
                label = label,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            ConfirmButton(
                text = "확인",
                onConfirm = { /* TODO */ },
                modifier = Modifier
                    .height(48.dp)
                    .align(Alignment.CenterVertically)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun SignUpViewPreview() {
    SignUpView(onSignUpSuccess = {})
}
