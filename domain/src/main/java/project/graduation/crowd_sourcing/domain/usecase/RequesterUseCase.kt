package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestDetail
import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestHistory
import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestPoint
import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestStats
import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestStatus
import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestSuccess
import project.graduation.crowd_sourcing.domain.repository.RequesterRepository
import javax.inject.Inject

/**
 * 의뢰자 관련 유스케이스
 */
class RequesterUseCase @Inject constructor(
    private val requesterRepository: RequesterRepository
) {
    /**
     * 의뢰 등록
     */
    suspend fun postRequest(
        commission: String,
        commissionCount: Int,
        commissionPoint: Int,
        commissionRegion: String,
        commissionDate: String,
        memberId: Int
    ): Int {
        return requesterRepository.postRequest(
            commission = commission,
            commissionCount = commissionCount,
            commissionPoint = commissionPoint,
            commissionRegion = commissionRegion,
            commissionDate = commissionDate,
            memberId = memberId
        )
    }
    
    /**
     * 회원별 의뢰 상태 조회
     * @param username 회원 아이디
     * @param status 조회할 의뢰 상태 (PROCESSING, COMPLETED, CANCEL 중 하나)
     */
    suspend fun getRequestStatus(
        username: String,
        status: String
    ): List<RequestStatus> {
        // 상태값 유효성 검사
        val validStatus = when (status.uppercase()) {
            "PROCESSING", "COMPLETED", "CANCEL" -> status.uppercase()
            else -> "PROCESSING" // 기본값
        }
        
        return requesterRepository.getRequestStatus(username, validStatus)
    }
    
    /**
     * 회원별 전체 의뢰 횟수 조회
     */
    suspend fun getRequestStats(username: String): RequestStats {
        return requesterRepository.getRequestStats(username)
    }
    
    /**
     * 회원별 총 사용 포인트 조회
     */
    suspend fun getRequestPoint(username: String): RequestPoint {
        return requesterRepository.getRequestPoint(username)
    }
    
    /**
     * 회원별 의뢰 성공 횟수 조회
     */
    suspend fun getRequestSuccess(username: String): RequestSuccess {
        return requesterRepository.getRequestSuccess(username)
    }
    
    /**
     * 회원별 가장 많이 의뢰한 지역 및 카테고리 조회
     */
    suspend fun getRequestDetail(username: String): RequestDetail {
        return requesterRepository.getRequestDetail(username)
    }
    
    /**
     * 진행중인 의뢰 목록 조회
     */
    suspend fun getOngoingRequests(username: String): List<RequestStatus> {
        return requesterRepository.getOngoingRequests(username)
    }
    
    /**
     * 완료/취소된 의뢰 기록 조회
     * @param status 조회할 의뢰 상태 (COMPLETED, CANCEL 중 하나)
     */
    suspend fun getRequestHistory(username: String, status: String): List<RequestHistory> {
        // 상태값 유효성 검사
        val validStatus = when (status.uppercase()) {
            "COMPLETED", "CANCEL" -> status.uppercase()
            else -> "COMPLETED" // 기본값
        }
        
        return requesterRepository.getRequestHistory(username, validStatus)
    }
} 