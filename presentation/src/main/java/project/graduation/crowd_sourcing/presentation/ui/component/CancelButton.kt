package project.graduation.crowd_sourcing.presentation.ui.component

import androidx.compose.foundation.border
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
fun CancelButton(
    text: String,
    onConfirm: () -> Unit
) {
    Button(
        onClick = { onConfirm() },
        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.border(1.dp, Color.Black, RoundedCornerShape(6.dp))
    ) {
        Text(text, color = Color.Black)
    }
}

@Preview
@Composable
fun CancelButtonPrev(){
    CancelButton(
        "cancel"
    ) { }
}