package project.graduation.crowd_sourcing.presentation.ui.screen.my

import android.graphics.Bitmap
import project.graduation.crowd_sourcing.presentation.utils.twoDaysAgo
import java.util.Calendar
import java.util.Date

data class MyUiState(
    val profileImage: Bitmap?,
    val nickname: String,
    val point: Int,
    val recentRequest: List<RecentListItem>,
    val recentWork: List<RecentListItem>,
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
            recentRequest = listOf(
                RecentListItem(name = "test1", date = twoDaysAgo),
                RecentListItem(name = "test2", date = twoDaysAgo),
                ),
            recentWork = listOf(
                RecentListItem(name = "test1", date = twoDaysAgo),
                RecentListItem(name = "test2", date = twoDaysAgo),
            )
        )
    }
}
