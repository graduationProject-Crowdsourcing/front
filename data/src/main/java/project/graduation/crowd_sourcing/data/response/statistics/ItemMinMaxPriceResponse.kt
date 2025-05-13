package project.graduation.crowd_sourcing.data.response.statistics

import com.google.gson.annotations.SerializedName

data class ItemMinMaxPriceResponse(
    @SerializedName("region") val region: String,
    @SerializedName("category") val category: String,
    @SerializedName("item") val item: String,
    @SerializedName("itemPrice") val itemPrice: Int,
    @SerializedName("mart") val mart: String,
    @SerializedName("averagePrice") val averagePrice: Int,
    @SerializedName("difference") val difference: Int
)
