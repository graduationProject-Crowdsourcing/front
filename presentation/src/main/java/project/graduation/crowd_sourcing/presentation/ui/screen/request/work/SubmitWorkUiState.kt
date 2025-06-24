package project.graduation.crowd_sourcing.presentation.ui.screen.request.work

import android.net.Uri
import java.util.Calendar

data class SubmitWorkUiState(
    val id: Int? = null,
    val title: String = "",
    val place: String = "",
    val price: String = "",
    val reward: Int = 0,
    val executeTime: String = getCurrentDateTimeString(),
    val imageUri: Uri? = null,
    val locationVerified: Boolean = false
)


private fun getCurrentDateTimeString(): String {
    val now = Calendar.getInstance()
    val year = now.get(Calendar.YEAR)
    val month = now.get(Calendar.MONTH) + 1 // 0-based
    val day = now.get(Calendar.DAY_OF_MONTH)
    val hour = now.get(Calendar.HOUR_OF_DAY)
    val minute = now.get(Calendar.MINUTE)
    return "%04d-%02d-%02d %02d:%02d".format(year, month, day, hour, minute)
}
