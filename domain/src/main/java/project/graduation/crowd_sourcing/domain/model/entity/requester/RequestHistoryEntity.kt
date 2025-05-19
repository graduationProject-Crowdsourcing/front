package project.graduation.crowd_sourcing.domain.model.entity.requester

import java.time.LocalDateTime
import java.util.Date

/**
 * 의뢰 히스토리 정보 도메인 모델
 */
data class RequestHistoryEntity(
    val id: Long,
    val commission: String,
    val commissionCount: Int,
    val commissionPoint: Int,
    val category: String,
    val commissionRegion: String,
    val commissionDate: LocalDateTime,
    val commissionStatus: String,
    val memberId: Int
) 