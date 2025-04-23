package project.graduation.crowd_sourcing.presentation.ui.screen.request.work

import androidx.navigation.NavHostController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.ConfirmButton

@Composable
fun WorkCompleteView(
    navController: NavController,
    place: String,
    title: String,
    reward: Int
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
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_check_circle),
                contentDescription = "완료 아이콘",
                modifier = Modifier.size(72.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                buildAnnotatedString {
                    append("${place}에서\n")
                    addStyle(
                        style = SpanStyle(color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold),
                        start = 0,
                        end = place.length
                    )

                    val start = length
                    append("${title}\n")
                    addStyle(
                        style = SpanStyle(color = Color(0xFF2196F3), fontWeight = FontWeight.Bold),
                        start = start,
                        end = start + title.length
                    )

                    append("작업을 완료했어요")
                },
                fontSize = 18.sp,
                lineHeight = 28.sp,
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "+ ${reward} 포인트 적립",
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            ConfirmButton(
                text = "확인",
                onConfirm = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
