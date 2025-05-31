package project.graduation.crowd_sourcing.data.response.my

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class RecentWorkResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("work") val work: String,
    @SerializedName("region") val region: String,
    @SerializedName("category") val category: String,
    @SerializedName("item") val item: String,
    @SerializedName("itemPrice") val itemPrice: Int,
    @SerializedName("workDate") val workDate: LocalDateTime
)