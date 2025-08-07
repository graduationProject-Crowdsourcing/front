package project.graduation.crowd_sourcing.presentation.ui.screen.my

import android.graphics.Bitmap
import project.graduation.crowd_sourcing.presentation.utils.twoDaysAgo
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date

data class MyUiState(
    val profileImage: String?,
    val nickname: String,
    val point: Int,
    val recentRequest: RecentListItem?,
    val recentWork: RecentListItem?,
    val isDialogVisible: Boolean = false
) {
    data class RecentListItem(
        val id:Int,
        val name: String,
        val region: String,
        val category: String,
        val date: LocalDateTime
    )

    companion object {
        fun init() = MyUiState(
            profileImage = null,
            nickname = "Unknown",
            point = 0,
            recentRequest = null,
            recentWork = null
        )
    }
}
