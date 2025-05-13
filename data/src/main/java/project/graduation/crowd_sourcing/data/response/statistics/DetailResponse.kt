package project.graduation.crowd_sourcing.data.response.statistics

import com.google.gson.annotations.SerializedName

data class DetailResponse(
    @SerializedName("commission") val commission: String,
    @SerializedName("commissionregion") val commissionregion: String,
    @SerializedName("category") val category: String,
    @SerializedName("commissionDate") val commissionDate: String,
    @SerializedName("commisionCount") val commisionCount: Int,
    @SerializedName("commisionpoint") val commisionpoint: Int
)
