package project.graduation.crowd_sourcing.data.response.worker

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class WorkHistoryResponse (
    @SerializedName("id") val id: Int,
    @SerializedName("commission") val commission: String,
    @SerializedName("commissionCount") val commissionCount: Int,
    @SerializedName("commissionPoint") val commissionPoint: Int,
    @SerializedName("commissionRegion") val commissionRegion: String,
    @SerializedName("commissionDate") val commissionDate: LocalDateTime,
    @SerializedName("commissionStatus") val commissionStatus: String,
    @SerializedName("memberId") val memberId: Int
)