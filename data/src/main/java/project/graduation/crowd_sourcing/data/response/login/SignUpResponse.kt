package project.graduation.crowd_sourcing.data.response.login

import com.google.gson.annotations.SerializedName

data class SignUpResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: SignUpUserData? = null
)

data class SignUpUserData(
    val id: Int,
    val username: String,
    val nickname: String,
    val role: String,
    val createdAt: String
)
