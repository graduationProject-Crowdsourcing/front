package project.graduation.crowd_sourcing.data.response.my

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class RecentWorkResponse(
    val assignmentId: Int,
    val workId: Int,
    val workName: String,
    val workStatus: String
)