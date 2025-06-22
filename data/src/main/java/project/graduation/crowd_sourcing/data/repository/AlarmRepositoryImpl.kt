package project.graduation.crowd_sourcing.data.repository

import project.graduation.crowd_sourcing.data.service.alarm.AlarmService
import project.graduation.crowd_sourcing.domain.repository.AlarmRepository
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val alarmService: AlarmService
) : AlarmRepository {

    override suspend fun postCancel(workId: Int, memberId: Int): Result<String> {
        return try {
            val response = alarmService.postCancel(workId, memberId)
            if (response.isSuccessful) {
                Result.success(response.body()?.string() ?: "작업이 취소되었습니다.")
            } else {
                Result.failure(Exception("작업 취소 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun postAccept(workId: Int, memberId: Int): Result<String> {
        return try {
            android.util.Log.d("AlarmRepository", "postAccept 호출: workId=$workId, memberId=$memberId")
            val response = alarmService.postAccept(workId, memberId)
            android.util.Log.d("AlarmRepository", "응답 코드: ${response.code()}")
            android.util.Log.d("AlarmRepository", "응답 성공 여부: ${response.isSuccessful}")
            
            if (response.isSuccessful) {
                val resultMessage = response.body()?.string() ?: "작업이 수락되었습니다."
                android.util.Log.d("AlarmRepository", "성공 처리: $resultMessage")
                Result.success(resultMessage)
            } else {
                val errorMessage = "작업 수락 실패: ${response.code()}"
                android.util.Log.e("AlarmRepository", errorMessage)
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            android.util.Log.e("AlarmRepository", "예외 발생: ${e.message}", e)
            Result.failure(e)
        }
    }

    override suspend fun getSendNotification(latitude: Double, longitude: Double): Result<String> {
        return try {
            val response = alarmService.getSendNotification(latitude, longitude)
            if (response.isSuccessful) {
                Result.success(response.body()?.string() ?: "알림이 전송되었습니다.")
            } else {
                Result.failure(Exception("알림 전송 실패: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 