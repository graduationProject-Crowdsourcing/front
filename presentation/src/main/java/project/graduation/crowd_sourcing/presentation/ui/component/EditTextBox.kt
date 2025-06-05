package project.graduation.crowd_sourcing.presentation.ui.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.graduation.crowd_sourcing.presentation.R


@Composable
fun EditTextBox(
    value: String,
    onValueChange: (String) -> Unit,
    placeHolder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val shape = RoundedCornerShape(dimensionResource(R.dimen.round_common))
    val borderColor = colorResource(id = R.color.light_gray)

    Log.d("EditTextBox", "EditTextBox 렌더링 됨: enabled=$enabled, readOnly=$readOnly, onClick=${onClick != null}")

    Box(
        modifier = modifier
            .height(dimensionResource(R.dimen.height_btn))
            .fillMaxWidth()
            .clip(shape)
            .border(1.dp, borderColor, shape)
            .background(Color.White)
            .clickable(
                enabled = onClick != null
            ) {
                Log.d("EditTextBox", "Box 클릭됨")
                onClick?.invoke()
            }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            singleLine = true,
            keyboardOptions = keyboardOptions.copy(keyboardType = keyboardType),
            keyboardActions = keyboardActions,
            textStyle = LocalTextStyle.current.copy(
                color = Color.Black,
                fontSize = 14.sp
            ),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeHolder,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                        innerTextField()
                    }
                    if (trailingIcon != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                        trailingIcon()
                    }
                }
            }
        )
    }
}


@Preview
@Composable
fun EditTextBoxPrev(){
    EditTextBox(
        value = "test",
        onValueChange = {},
        placeHolder = "test"
    )
}

