package project.graduation.crowd_sourcing.presentation.ui.screen.my.component

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import project.graduation.crowd_sourcing.presentation.R

@Composable
fun GrayDivider() {
    HorizontalDivider(color = colorResource(R.color.gray), thickness = 1.dp)
}