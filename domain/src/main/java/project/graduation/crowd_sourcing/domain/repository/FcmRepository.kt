package project.graduation.crowd_sourcing.domain.repository

interface FcmRepository {
    suspend fun poseSend(
        memberId: Int,
        title: String,
        body: String
    ): Result<Unit>

    suspend fun postRegister(
        memberId: Int,
        fcmToken: String
    ): Result<Unit>


    suspend fun postCancel(
        workId: Int,
        memberId: Int
    ): Result<Unit>

    suspend fun postAccept(
        workId: Int,
        memberId: Int
    ): Result<Unit>

    suspend fun postRejectWork(
        workId: Int,
        memberId: Int
    ): Result<Unit>

    suspend fun getSendNotifications(
        latitude: Double,
        longitude: Double
    ): Result<Unit>
}