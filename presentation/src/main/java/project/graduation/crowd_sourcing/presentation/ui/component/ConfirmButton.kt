package project.graduation.crowd_sourcing.presentation.ui.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import project.graduation.crowd_sourcing.presentation.R

@Composable
fun ConfirmButton(
    modifier: Modifier = Modifier,
    text: String,
    onConfirm: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = { onConfirm() },
        modifier = modifier.height(dimensionResource(R.dimen.height_btn)),
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.round_common)),
        enabled = enabled
    ) {
        Text(text, color = Color.White)
    }
}

/* 
@Preview
@Composable
fun ConfirmButtonPrev() {
    ConfirmButton(
        text = "apply"
    ) { }
}

 */