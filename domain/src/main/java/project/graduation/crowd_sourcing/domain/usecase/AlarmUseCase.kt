package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.domain.local.TokenManager
import project.graduation.crowd_sourcing.domain.repository.FcmRepository
import project.graduation.crowd_sourcing.domain.repository.LocationRepository
import javax.inject.Inject

class AlarmUseCase @Inject constructor(
    private val fcmRepository: FcmRepository,
    private val locationRepository: LocationRepository,
    private val tokenManager: TokenManager
) {
    suspend fun updateLocation(latitude: Double, longitude: Double): Result<Unit>{
        return  try {
            tokenManager.getUserId()?.let{
                locationRepository.updateLocation(
                    memberId = it,
                    latitude = latitude,
                    longitude = longitude
                )
            } ?: Result.failure(IllegalStateException("로그인된 사용자 정보가 없습니다."))
        }catch (e:Exception){
            Result.failure(e)
        }

    }
}