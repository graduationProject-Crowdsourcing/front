package project.graduation.crowd_sourcing.presentation.ui.screen.alarm

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material.Switch
import androidx.wear.compose.material.SwitchDefaults
import androidx.wear.compose.material.Text
import project.graduation.crowd_sourcing.presentation.R

@Composable
fun AlarmView() {
    val viewModel: AlarmViewModel = hiltViewModel()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.space_medium))
    ) {
        AlarmSwitchRow(
            isAlarmOn = viewModel.isChecked,
            onToggleAlarm = {viewModel.toggleChecked()}
        )
    }
}

@Composable
fun AlarmSwitchRow(
    isAlarmOn: Boolean,
    onToggleAlarm: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "알람 설정",
            style = TextStyle(
                fontSize = dimensionResource(id = R.dimen.sp_large).value.sp,
                fontWeight = FontWeight.Bold
            ),
            color = Color.Black
        )

        CustomSwitch(
            checked = isAlarmOn,
            onCheckedChange = onToggleAlarm
        )
//        Switch(
//            checked = isAlarmOn,
//            onCheckedChange = { onToggleAlarm(it) },
//            colors = SwitchDefaults.colors(
//                checkedThumbColor = Color.White,
//                checkedTrackColor = Color.Black,
//                uncheckedThumbColor = Color.White,
//                uncheckedTrackColor = colorResource(R.color.gray)
//            )
//        )
    }
}


@Composable
fun CustomSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    val transition = updateTransition(targetState = checked, label = "Switch Transition")
    val thumbOffset by transition.animateDp(label = "Thumb Offset") {
        if (it) 20.dp else 0.dp
    }

    Box(
        modifier = Modifier
            .width(48.dp)
            .height(28.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(if (checked) Color.Black else Color.LightGray)
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .padding(2.dp)
                .offset(x = thumbOffset)
                .size(24.dp)
                .background(Color.White, shape = CircleShape)
        )
    }
}



@Preview
@Composable
fun AlarmViewPrev() {
    AlarmView()
}