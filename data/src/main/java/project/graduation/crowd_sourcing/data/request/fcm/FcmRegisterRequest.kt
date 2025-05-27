package project.graduation.crowd_sourcing.data.request.fcm

data class FcmRegisterRequest(
    val memberId: Long,
    val fcmToken: String
)
