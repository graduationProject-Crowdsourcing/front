package project.graduation.crowd_sourcing.presentation.ui.screen.my.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.utils.spaceMedium
import project.graduation.crowd_sourcing.presentation.utils.spaceSmall
import project.graduation.crowd_sourcing.presentation.utils.textStyleMedium

@Composable
fun MyEtc() {
    val list = listOf(
        (R.drawable.ic_set to "알람 설정") to {},
        (R.drawable.ic_report to "신고하기") to {},
        (R.drawable.ic_support to "고객지원") to {},
        (R.drawable.ic_out to "로그아웃") to {},
        (R.drawable.img_withdraw to "탈퇴하기") to {}
    )

    Column(
        modifier = Modifier.padding(vertical = spaceSmall())
    ) {
        list.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { item.second.invoke() }
                    .padding(vertical = spaceSmall()),
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

                Spacer(modifier = Modifier.width(spaceSmall()))

                Text(
                    text = item.first.second,
                    style = textStyleMedium(),
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
    MyEtc()
}

