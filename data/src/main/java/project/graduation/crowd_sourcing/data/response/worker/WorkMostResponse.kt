package project.graduation.crowd_sourcing.data.response.worker

import com.google.gson.annotations.SerializedName

data class WorkMostResponse(
    @SerializedName("region") val region: String,
    @SerializedName("item") val item: String,
    @SerializedName("dayOfWeek") val dayOfWeek: String
)