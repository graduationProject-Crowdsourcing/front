package project.graduation.crowd_sourcing.data.service

import project.graduation.crowd_sourcing.data.request.UpdateLocationRequest
import retrofit2.http.Body
import retrofit2.http.PATCH

interface LocationService {
    @PATCH("/api/v1/members/update-location")
    suspend fun updateLocation(
        @Body request: UpdateLocationRequest
    )
}