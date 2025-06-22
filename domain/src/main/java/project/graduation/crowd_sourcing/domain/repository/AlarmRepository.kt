package project.graduation.crowd_sourcing.domain.repository

interface AlarmRepository {
    suspend fun postCancel(workId: Int, memberId: Int): Result<String>
    suspend fun postAccept(workId: Int, memberId: Int): Result<String>
    suspend fun getSendNotification(latitude: Double, longitude: Double): Result<String>
} 