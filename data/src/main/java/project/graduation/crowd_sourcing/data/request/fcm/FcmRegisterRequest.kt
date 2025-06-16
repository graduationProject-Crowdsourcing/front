package project.graduation.crowd_sourcing.data.request.fcm

data class FcmRegisterRequest(
    val memberId: Int,
    val fcmToken: String
)
