package project.graduation.crowd_sourcing.data.request.worker

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class PostWorkRequest(
    @SerializedName("work") val work: String,
    @SerializedName("workCount") val workCount: Int,
    @SerializedName("workpoint") val workPoint: Int,
    @SerializedName("region") val region: String,
    @SerializedName("item") val item: String,
    @SerializedName("itemPrice") val itemPrice: Int,
    @SerializedName("workDate") val workDate: String,
    @SerializedName("memberId") val memberId: Int
)