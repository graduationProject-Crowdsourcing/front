package project.graduation.crowd_sourcing.presentation.ui.screen.my

import android.graphics.Bitmap
import project.graduation.crowd_sourcing.presentation.utils.twoDaysAgo
import java.util.Calendar
import java.util.Date

data class MyUiState(
    val profileImage: Bitmap?,
    val nickname: String,
    val point: Int,
    val recentRequest: RecentListItem,
    val recentWork: RecentListItem,
    val isDialogVisible: Boolean = false
) {
    data class RecentListItem(
        val name: String,
        val date: Date
    )

    companion object {
        fun init() = MyUiState(
            profileImage = null,
            nickname = "user name",
            point = 0,
            recentRequest = RecentListItem(name = "test1", date = twoDaysAgo),
            recentWork = RecentListItem(name = "test1", date = twoDaysAgo)
        )
    }
}
