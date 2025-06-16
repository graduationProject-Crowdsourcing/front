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
    val recentRequest: RecentListItem,
    val recentWork: RecentListItem,
    val isDialogVisible: Boolean = false
) {
    data class RecentListItem(
        val id:Int,
        val name: String,
        val date: LocalDateTime
    )

    companion object {
        fun init() = MyUiState(
            profileImage = null,
            nickname = "",
            point = 0,
            recentRequest = RecentListItem(0,name = "test1", date = twoDaysAgo),
            recentWork = RecentListItem(0,name = "test1", date = twoDaysAgo)
        )
    }
}
