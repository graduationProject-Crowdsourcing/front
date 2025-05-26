package project.graduation.crowd_sourcing.domain.repository

import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestDetailEntity
import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestHistoryEntity
import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestPointEntity
import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestStatsEntity
import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestStatusEntity
import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestSuccessEntity

interface RequesterRepository {
    /**
     * 의뢰를 등록합니다
     */
    suspend fun postRequest(
        commission: String,
        commissionCount: Int,
        commissionPoint: Int,
        commissionRegion: String,
        commissionDate: String,
        memberId: Int
    ): Int

    /**
     * 회원별 의뢰 상태를 조회합니다
     */
    suspend fun getRequestStatus(
        username: String,
        status: String
    ): List<RequestStatusEntity>
    
    /**
     * 회원별 전체 의뢰 횟수를 조회합니다
     */
    suspend fun getRequestStats(username: String): RequestStatsEntity
    
    /**
     * 회원별 총 사용 포인트를 조회합니다
     */
    suspend fun getRequestPoint(username: String): RequestPointEntity
    
    /**
     * 회원별 의뢰 성공 횟수를 조회합니다
     */
    suspend fun getRequestSuccess(username: String): RequestSuccessEntity
    
    /**
     * 회원별 가장 많이 의뢰한 지역 및 카테고리를 조회합니다
     */
    suspend fun getRequestDetail(username: String): RequestDetailEntity
    
    /**
     * 진행중인 의뢰 목록을 조회합니다
     */
    suspend fun getOngoingRequests(username: String): List<RequestStatusEntity>
    
    /**
     * 완료/취소된 의뢰 기록을 조회합니다
     */
    suspend fun getRequestHistory(username: String, status: String): List<RequestHistoryEntity>
} 