package project.graduation.crowd_sourcing.domain.model.entity.requester

import java.time.LocalDateTime
import java.util.Date

/**
 * 의뢰 상태 정보 도메인 모델
 */
data class RequestStatusEntity(
    val id: Long,
    val commission: String,
    val commissionCount: Int,
    val commissionPoint: Int,
    val commissionRegion: String,
    val commissionDate: LocalDateTime,
    val commissionStatus: String,
    val commissionCategory: String,
    val memberId: Int
) 