package project.graduation.crowd_sourcing.data.response.userpoint

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class UserPointHistoryResponse(
    @SerializedName("type") val type: String,
    @SerializedName("region") val region: String,
    @SerializedName("item") val item: String,
    @SerializedName("point") val point: Int,
    @SerializedName("date") val date: LocalDateTime
)
