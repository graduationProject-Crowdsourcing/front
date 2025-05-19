package project.graduation.crowd_sourcing.data.response.requester

import java.time.LocalDateTime
import java.util.Date

/**
 * 의뢰 등록 요청 DTO
 */
data class RequestDto(
    val commission: String,
    val commissionCount: Int,
    val commissionPoint: Int,
    val commissionRegion: String,
    val commissionDate: String,
    val memberId: Int
)

/**
 * 의뢰 상태 조회 응답 DTO
 */
data class RequestStatusDto(
    val id: Long,
    val commission: String,
    val commissionCount: Int,
    val commissionPoint: Int,
    val commissionRegion: String,
    val commissionDate: LocalDateTime,
    val commissionStatus: String,
    val memberId: Int
)

/**
 * 전체 의뢰 횟수 응답 DTO
 */
data class RequestStatsDto(
    val commsionCount: Int
)

/**
 * 총 사용 포인트 응답 DTO
 */
data class RequestPointDto(
    val totalPoints: Int
)

/**
 * 의뢰 성공 횟수 응답 DTO
 */
data class RequestSuccessDto(
    val successfulCommissions: Int
)

/**
 * 가장 많이 의뢰한 지역 및 요일 응답 DTO
 */
data class RequestDetailDto(
    val mostRequestedRegion: String,
    val mostRequestedDayOfWeek: String
)

/**
 * 의뢰 히스토리 응답 DTO
 */
data class RequestHistoryDto(
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