package project.graduation.crowd_sourcing.domain.repository

import project.graduation.crowd_sourcing.domain.model.entity.my.RecentCommissionEntity
import project.graduation.crowd_sourcing.domain.model.entity.my.RecentWorkEntity

interface MyRepository {
    suspend fun getRecentWork(userId: Int): Result<RecentWorkEntity>

    suspend fun getRecentCommission(userId: Int): Result<RecentCommissionEntity>
}