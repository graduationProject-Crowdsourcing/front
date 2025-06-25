package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestDetailEntity
import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestHistoryEntity
import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestPointEntity
import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestStatsEntity
import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestStatusEntity
import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestSuccessEntity
import project.graduation.crowd_sourcing.domain.repository.RequesterRepository
import project.graduation.crowd_sourcing.domain.repository.WorkerRepository
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * 의뢰자 관련 유스케이스
 */
class RequesterUseCase @Inject constructor(
    private val requesterRepository: RequesterRepository,
    private val workerRepository: WorkerRepository
) {

    //의뢰 등록 TODO : 확인 후 삭제 예정
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

    // 의뢰 생성 (Swagger 기반 포맷)
    suspend fun postWork(
        work: String,
        workCount: Int,
        workpoint: Int,
        martName: String,
        sigungu: String,
        item: String,
        workDate: String,
        memberId: Int,
        category: String,
        workhour: Int,
        expirationDate: String
    ): Result<Int> {
        return workerRepository.postWork(
            work = work,
            workCount = workCount,
            workpoint = workpoint,
            martName = martName,
            sigungu = sigungu,
            item = item,
            workDate = workDate,
            memberId = memberId,
            category = category,
            workhour = workhour,
            expirationDate = expirationDate
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
    ): List<RequestStatusEntity> {
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
    suspend fun getRequestStats(username: String): RequestStatsEntity {
        return requesterRepository.getRequestStats(username)
    }
    
    /**
     * 회원별 총 사용 포인트 조회
     */
    suspend fun getRequestPoint(username: String): RequestPointEntity {
        return requesterRepository.getRequestPoint(username)
    }
    
    /**
     * 회원별 의뢰 성공 횟수 조회
     */
    suspend fun getRequestSuccess(username: String): RequestSuccessEntity {
        return requesterRepository.getRequestSuccess(username)
    }
    
    /**
     * 회원별 가장 많이 의뢰한 지역 및 카테고리 조회
     */
    suspend fun getRequestDetail(username: String): RequestDetailEntity {
        return requesterRepository.getRequestDetail(username)
    }
    
    /**
     * 진행중인 의뢰 목록 조회
     */
    suspend fun getOngoingRequests(username: String): List<RequestStatusEntity> {
        return requesterRepository.getOngoingRequests(username)
    }
    
    /**
     * 완료/취소된 의뢰 기록 조회
     * @param status 조회할 의뢰 상태 (COMPLETED, CANCEL 중 하나)
     */
    suspend fun getRequestHistory(username: String, status: String): List<RequestHistoryEntity> {
        // 상태값 유효성 검사
        val validStatus = when (status.uppercase()) {
            "COMPLETED", "CANCEL" -> status.uppercase()
            else -> "COMPLETED" // 기본값
        }
        
        return requesterRepository.getRequestHistory(username, validStatus)
    }
} 