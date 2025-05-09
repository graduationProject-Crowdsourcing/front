package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.domain.model.entity.my.RecentCommissionEntity
import project.graduation.crowd_sourcing.domain.model.entity.my.RecentWorkEntity
import project.graduation.crowd_sourcing.domain.repository.MyRepository
import javax.inject.Inject

class MyUseCase @Inject constructor(
    private val repository: MyRepository
) {
    suspend fun getRecentHistory(userId :Int): Pair<RecentWorkEntity, RecentCommissionEntity>{
        val recentWork = repository.getRecentWork(userId)
        val recentCommission = repository.getRecentCommission(userId)

        return Pair(recentWork, recentCommission)
    }
}