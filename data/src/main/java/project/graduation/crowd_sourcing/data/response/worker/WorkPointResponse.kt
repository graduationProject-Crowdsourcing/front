package project.graduation.crowd_sourcing.data.response.worker

import com.google.gson.annotations.SerializedName

data class WorkPointResponse(
    @SerializedName("totalPoints") val totalPoints: Int
)