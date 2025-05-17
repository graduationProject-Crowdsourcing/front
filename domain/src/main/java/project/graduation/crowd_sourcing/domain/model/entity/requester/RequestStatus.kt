package project.graduation.crowd_sourcing.domain.model.entity.requester

import java.util.Date

/**
 * 의뢰 상태 정보 도메인 모델
 */
data class RequestStatus(
    val id: Long,
    val commission: String,
    val commissionCount: Int,
    val commissionPoint: Int,
    val commissionRegion: String,
    val commissionDate: Date,
    val commissionStatus: String,
    val memberId: Int
) 