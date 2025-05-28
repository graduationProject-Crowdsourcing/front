package project.graduation.crowd_sourcing.data.repository

import project.graduation.crowd_sourcing.data.response.requester.RequestDto
import project.graduation.crowd_sourcing.data.service.RequesterService
import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestDetailEntity
import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestHistoryEntity
import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestPointEntity
import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestStatsEntity
import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestStatusEntity
import project.graduation.crowd_sourcing.domain.model.entity.requester.RequestSuccessEntity
import project.graduation.crowd_sourcing.domain.repository.RequesterRepository
import java.util.Date
import java.util.TimeZone
import javax.inject.Inject

class RequesterRepositoryImpl @Inject constructor(
    private val requesterService: RequesterService
) : RequesterRepository {

    override suspend fun postRequest(
        commission: String,
        commissionCount: Int,
        commissionPoint: Int,
        commissionRegion: String,
        commissionDate: String,
        memberId: Int
    ): Int {
        // 디버깅 정보 출력
        println("DEBUG_DATE: 서버로 보내는 날짜 (ISO 포맷): $commissionDate")
        println("DEBUG_DATE: 날짜 형식 확인: Z 포함 여부: ${commissionDate.contains('Z')}")
        println("DEBUG_DATE: 현재 시스템 시간: ${Date()}")
        println("DEBUG_DATE: 현재 타임존: ${TimeZone.getDefault().id}, 오프셋: ${TimeZone.getDefault().rawOffset / (1000 * 60 * 60)}시간")
        
        val requestDto = RequestDto(
            commission = commission,
            commissionCount = commissionCount,
            commissionPoint = commissionPoint,
            commissionRegion = commissionRegion,
            commissionDate = commissionDate,
            memberId = memberId
        )
        
        println("DEBUG_DATE: 요청 DTO 내용: commission='$commission', count=$commissionCount, point=$commissionPoint, region='$commissionRegion', memberId=$memberId")
        println("DEBUG_DATE: DTO에 포함된 날짜 객체: ${requestDto.commissionDate}")
        
        return try {
            // API 호출
            val response = requesterService.postRequest(requestDto)
            println("DEBUG_DATE: 요청 성공, 응답 ID: $response")
            response
        } catch (e: Exception) {
            // 오류 정보 로깅
            println("DEBUG_DATE: 의뢰 등록 오류: ${e.message}")
            println("DEBUG_DATE: 오류 클래스: ${e.javaClass.name}")
            e.printStackTrace()
            
            // 다시 예외 던지기 (상위 레이어에서 처리)
            throw e
        }
    }

    override suspend fun getRequestStatus(
        username: String, 
        status: String
    ): List<RequestStatusEntity> {
        return try {
            val response = requesterService.getRequestStatus(username, status)
            response.map { dto ->
                RequestStatusEntity(
                    id = dto.id,
                    commission = dto.commission,
                    commissionCount = dto.commissionCount,
                    commissionPoint = dto.commissionPoint,
                    commissionRegion = dto.commissionRegion,
                    commissionDate = dto.commissionDate,
                    commissionStatus = dto.commissionStatus,
                    memberId = dto.memberId
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    override suspend fun getRequestStats(username: String): RequestStatsEntity {
        return try {
            val response = requesterService.getRequestStats(username)
            RequestStatsEntity(
                commissionCount = response.commsionCount
            )
        } catch (e: Exception) {
            RequestStatsEntity(0)
        }
    }
    
    override suspend fun getRequestPoint(username: String): RequestPointEntity {
        return try {
            val response = requesterService.getRequestPoint(username)
            RequestPointEntity(
                totalPoints = response.totalPoints
            )
        } catch (e: Exception) {
            RequestPointEntity(0)
        }
    }
    
    override suspend fun getRequestSuccess(username: String): RequestSuccessEntity {
        return try {
            val response = requesterService.getRequestSuccess(username)
            RequestSuccessEntity(
                successfulCommissions = response.successfulCommissions
            )
        } catch (e: Exception) {
            RequestSuccessEntity(0)
        }
    }
    
    override suspend fun getRequestDetail(username: String): RequestDetailEntity {
        return try {
            val response = requesterService.getRequestDetail(username)
            RequestDetailEntity(
                mostRequestedRegion = response.mostRequestedRegion,
                mostRequestedCategory = response.mostRequestedCategory
            )
        } catch (e: Exception) {
            RequestDetailEntity("", "")
        }
    }
    
    override suspend fun getOngoingRequests(username: String): List<RequestStatusEntity> {
        return try {
            val response = requesterService.getOngoingRequests(username)
            response.map { dto ->
                RequestStatusEntity(
                    id = dto.id,
                    commission = dto.commission,
                    commissionCount = dto.commissionCount,
                    commissionPoint = dto.commissionPoint,
                    commissionRegion = dto.commissionRegion,
                    commissionDate = dto.commissionDate,
                    commissionStatus = dto.commissionStatus,
                    memberId = dto.memberId
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    override suspend fun getRequestHistory(username: String, status: String): List<RequestHistoryEntity> {
        return try {
            val response = requesterService.getRequestHistory(username, status)
            response.map { dto ->
                RequestHistoryEntity(
                    id = dto.id,
                    commission = dto.commission,
                    commissionCount = dto.commissionCount,
                    commissionPoint = dto.commissionPoint,
                    category = dto.category,
                    commissionRegion = dto.commissionRegion,
                    commissionDate = dto.commissionDate,
                    commissionStatus = dto.commissionStatus,
                    memberId = dto.memberId
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
} 