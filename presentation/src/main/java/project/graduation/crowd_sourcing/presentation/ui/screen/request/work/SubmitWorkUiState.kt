package project.graduation.crowd_sourcing.presentation.ui.screen.request.work

import android.net.Uri

data class SubmitWorkUiState(
    val id: Int? = null,
    val title: String = "",
    val place: String = "",
    val price: String = "",
    val reward: Int = 0,
    val executeTime: String = "",
    val imageUri: Uri? = null,
    val locationVerified: Boolean = false
)
