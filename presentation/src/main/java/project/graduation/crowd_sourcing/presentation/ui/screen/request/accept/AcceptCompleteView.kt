package project.graduation.crowd_sourcing.presentation.ui.screen.request.accept

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.ConfirmButton

// 의뢰 수락 완료 페이지
@Composable
fun AcceptCompleteView(
    navController: NavController,
    place: String,
    title: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_check_circle),
                contentDescription = "완료 아이콘",
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(buildAnnotatedString {
                withStyle(SpanStyle(color = Color(0xFF2C7D2C), fontWeight = FontWeight.Bold)) {
                    append("$place")
                }
                append(" 에서\n")
                withStyle(SpanStyle(color = Color(0xFF0B4EDB), fontWeight = FontWeight.Bold)) {
                    append("$title")
                }
                append(" 의뢰를 수락했어요")
            },
                fontSize = 18.sp,
                lineHeight = 28.sp,
                textAlign = TextAlign.Center
            )
        }

        ConfirmButton(
            text = "확인",
            onConfirm = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
    }
}
