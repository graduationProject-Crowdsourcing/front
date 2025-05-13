package project.graduation.crowd_sourcing.data.response.statistics

import com.google.gson.annotations.SerializedName

data class MartMinMaxPriceResponse(
    @SerializedName("region") val region: String,
    @SerializedName("category") val category: String,
    @SerializedName("mart") val mart: String,
    @SerializedName("categoryPrice") val categoryPrice: Int,
    @SerializedName("difference") val difference: Int
)
