package project.graduation.crowd_sourcing.presentation.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import project.graduation.crowd_sourcing.presentation.R

@Composable
fun EditTextBox(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(label) },
        singleLine = true,
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(dimensionResource(R.dimen.round_common)),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = colorResource(id = R.color.light_gray),
            unfocusedBorderColor = colorResource(id = R.color.light_gray),
            cursorColor = Color.Black
        )
    )
}

@Preview
@Composable
fun EditTextBoxPrev(){
    EditTextBox(
        value = "test",
        onValueChange = {},
        label = "test"
    )
}
