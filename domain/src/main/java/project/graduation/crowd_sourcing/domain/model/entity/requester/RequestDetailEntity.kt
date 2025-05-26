package project.graduation.crowd_sourcing.domain.model.entity.requester

/**
 * 회원별 가장 많이 의뢰한 지역 및 카테고리 정보
 */
data class RequestDetailEntity(
    val mostRequestedRegion: String,
    val mostRequestedDayOfWeek: String
) 