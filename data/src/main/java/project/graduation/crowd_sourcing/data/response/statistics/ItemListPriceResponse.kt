package project.graduation.crowd_sourcing.data.response.statistics

import com.google.gson.annotations.SerializedName

data class ItemListPriceResponse(
    @SerializedName("region") val region: String,
    @SerializedName("category") val category: String,
    @SerializedName("item") val item: String,
    @SerializedName("averagePrice") val averagePrice: Int
)
