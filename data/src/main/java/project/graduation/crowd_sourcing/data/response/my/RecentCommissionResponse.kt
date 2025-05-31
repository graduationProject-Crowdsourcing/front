package project.graduation.crowd_sourcing.data.response.my

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class RecentCommissionResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("commission") val commission: String,
    @SerializedName("region") val region: String,
    @SerializedName("category") val category: String,
    @SerializedName("commissionDate") val commissionDate: LocalDateTime,
    @SerializedName("commissionPoint") val commissionPoint: Int
)
