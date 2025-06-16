package project.graduation.crowd_sourcing.data.repository

import project.graduation.crowd_sourcing.data.request.UpdateLocationRequest
import project.graduation.crowd_sourcing.data.service.LocationService
import project.graduation.crowd_sourcing.domain.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationService: LocationService
): LocationRepository {
    override suspend fun updateLocation(memberId: Int, latitude: Double, longitude: Double): Result<Unit> {
        return  try {
            locationService.updateLocation(
                UpdateLocationRequest(
                    memberId = memberId,
                    latitude = latitude,
                    longitude = longitude
                )
            )
            Result.success(Unit)
        } catch (e:Exception){
            Result.failure(e)
        }
    }
}