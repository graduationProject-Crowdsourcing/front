package project.graduation.crowd_sourcing.data.request.fcm

data class FcmSendRequest(
    val memberId: Int,
    val title: String,
    val body: String
)
