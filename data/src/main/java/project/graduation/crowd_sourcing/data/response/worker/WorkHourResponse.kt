package project.graduation.crowd_sourcing.data.response.worker

import com.google.gson.annotations.SerializedName

data class WorkHourResponse(
    @SerializedName("workhour") val workhour: Int
)