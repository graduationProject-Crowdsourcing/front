package project.graduation.crowd_sourcing.data.response.statistics

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class DetailResponse(
    @SerializedName("commission") val commission: String,
    @SerializedName("commissionregion") val commissionregion: String,
    @SerializedName("category") val category: String,
    @SerializedName("createdAt") val commissionDate: LocalDateTime,
    @SerializedName("expirationDate") val expirationDate: LocalDateTime,
    @SerializedName("commisionCount") val commisionCount: Int,
    @SerializedName("commisionpoint") val commisionpoint: Int
)
