package project.graduation.crowd_sourcing.domain.repository

import project.graduation.crowd_sourcing.domain.model.entity.userpoint.UserPointHistoryEntity

interface UserPointRepository {
    suspend fun getUserPointHistory(memberId: Int): Result<List<UserPointHistoryEntity>>
}