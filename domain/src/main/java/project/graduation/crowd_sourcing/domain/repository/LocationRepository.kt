package project.graduation.crowd_sourcing.domain.repository

interface LocationRepository {
    suspend fun updateLocation(
        memberId: Int,
        latitude: Double,
        longitude: Double
    ): Result<Unit>
}