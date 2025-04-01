package project.graduation.crowd_sourcing.presentation.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import project.graduation.crowd_sourcing.presentation.R

@Composable
fun ConfirmButton(
    modifier: Modifier = Modifier,
    text: String,
    onConfirm: () -> Unit
) {
    Button(
        onClick = { onConfirm() },
        colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary)),
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(text, color = Color.White)
    }
}

@Preview
@Composable
fun ConfirmButtonPrev(){
    ConfirmButton(
        text = "apply"
    ) { }
}