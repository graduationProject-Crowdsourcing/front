package project.graduation.crowd_sourcing.presentation.ui.screen.my.component

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.CancelButton
import project.graduation.crowd_sourcing.presentation.ui.component.ConfirmButton
import project.graduation.crowd_sourcing.presentation.ui.screen.my.MyUiState

@Composable
fun MyProfileEditDialog(
    uiState: MyUiState,
    onDismiss: () -> Unit,
    onSave: (String, Bitmap?) -> Unit
) {
    var nickname by remember { mutableStateOf(uiState.nickname) }
    var profileImage by remember { mutableStateOf(uiState.profileImage) }
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                try {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    profileImage = bitmap
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    )

    if (uiState.isDialogVisible) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { },
            title = {
                Text(
                    "프로필 수정",
                    style = TextStyle(fontSize = dimensionResource(id = R.dimen.sp_title).value.sp)
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(1.dp, colorResource(R.color.gray), CircleShape) // 테두리 적용
                            .clickable {
                                imagePickerLauncher.launch("image/*")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (profileImage != null) {
                            Image(
                                bitmap = profileImage!!.asImageBitmap(),
                                contentDescription = "profile image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(R.drawable.ic_my),
                                contentDescription = "profile image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }


                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_medium)))

                    TextField(
                        value = nickname,
                        onValueChange = { nickname = it },
                        label = { Text("닉네임") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_small))
                ) {
                    CancelButton(
                        modifier = Modifier.weight(1f),
                        text = "취소") { onDismiss() }
                    ConfirmButton(
                        modifier = Modifier.weight(1f),
                        text = "수정") { }
                }
            },
            dismissButton = {

            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileEditDialogPreview() {
    MyProfileEditDialog(
        uiState = MyUiState.init(),
        onDismiss = {},
        onSave = { nickname, image -> /* Handle save */ }
    )
}
