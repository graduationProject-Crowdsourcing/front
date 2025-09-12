package project.graduation.crowd_sourcing.data.repository

import project.graduation.crowd_sourcing.data.request.fcm.FcmRegisterRequest
import project.graduation.crowd_sourcing.data.request.fcm.FcmSendRequest
import project.graduation.crowd_sourcing.data.request.fcm.FcmWorkRequest
import project.graduation.crowd_sourcing.data.service.alarm.FcmService
import project.graduation.crowd_sourcing.domain.repository.FcmRepository
import javax.inject.Inject

class FcmRepositoryImpl @Inject constructor(
    private val fcmService: FcmService
) : FcmRepository {
    override suspend fun poseSend(memberId: Int, title: String, body: String): Result<Unit> {
        return try {
            val response = fcmService.poseSend(
                request = FcmSendRequest(memberId, title, body)
            )
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("error"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun postRejectWork(workId: Int, memberId: Int): Result<Unit> {
        return try {
            val response = fcmService.postRejectWork(
                memberId = memberId, workId = workId
            )
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("error"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun postRegister(memberId: Int, fcmToken: String): Result<Unit> {
        return try {
            val response = fcmService.postRegister(memberId = memberId, fcmToken = fcmToken)

            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("error"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun postCancel(workId: Int, memberId: Int): Result<Unit> {
        return try {
            val response = fcmService.postCancel(
               memberId = memberId, workId = workId
            )
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("error"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun postAccept(workId: Int, memberId: Int): Result<Unit> {
        return try {
            val response = fcmService.postAccept(memberId = memberId, workId = workId)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("error"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getSendNotifications(latitude: Double, longitude: Double): Result<Unit> {
        return try {
            val response = fcmService.getSendNotifications(
                latitude = latitude,
                longitude = longitude
            )
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("error"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}