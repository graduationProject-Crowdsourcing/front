package project.graduation.crowd_sourcing.presentation.ui.screen.my.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen

@Composable
fun MyEtc(navController: NavController) {
    val list = listOf(
        (R.drawable.ic_set to "알람 설정") to {
            navController.navigate(Screen.AlarmSettingScreen.route)
        },
        (R.drawable.ic_report to "신고하기") to {},
        (R.drawable.ic_support to "고객지원") to {},
        (R.drawable.ic_out to "로그아웃") to {
             navController.navigate(Screen.LogoutConfirmScreen.route)
        },
        (R.drawable.img_withdraw to "탈퇴하기") to {}
    )

    Column(
        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.space_small))
    ) {
        list.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { item.second.invoke() }
                    .padding(vertical = dimensionResource(R.dimen.space_small)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (item.first.first == R.drawable.img_withdraw) {
                    Image(
                        bitmap = ImageBitmap.imageResource(id = item.first.first),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Icon(
                        painter = painterResource(id = item.first.first),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.space_small)))

                Text(
                    text = item.first.second,
                    style = TextStyle(fontSize = dimensionResource(id = R.dimen.sp_medium).value.sp),
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    painter = painterResource(R.drawable.ic_left),
                    contentDescription = null,
                    modifier = Modifier.graphicsLayer(scaleX = -1f)
                )
            }
        }
    }
}

@Preview
@Composable
fun MyEtcPrev() {
    MyEtc(rememberNavController())
}

