package project.graduation.crowd_sourcing.data.response.login

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String
)
