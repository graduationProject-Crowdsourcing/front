package project.graduation.crowd_sourcing.data.service

import project.graduation.crowd_sourcing.data.response.ApiResponseDtoListMartDto
import project.graduation.crowd_sourcing.data.response.CommissionDto
import project.graduation.crowd_sourcing.data.response.MartDto
import project.graduation.crowd_sourcing.data.response.SearchCommissionResponse
import project.graduation.crowd_sourcing.data.response.SearchHomeDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MartSearchService {
    @POST("/api/v1/marts/search/zipcode")
    suspend fun getSearchMartByZipcode(
        @Query("zipcode") zipcode: String,
        @Query("radius") radius: Int,
    ): ApiResponseDtoListMartDto

    @POST("/api/v1/marts/search/location")
    suspend fun getSearchMartByLocation(
        @Query("lat") lat : Double,
        @Query("lng") lng : Double,
        @Query("radius") radius: Int,
    ): ApiResponseDtoListMartDto

    @POST("/api/v1/marts/search/keyword")
    suspend fun getSearchMartByKeyword(
        @Query("keyword") keyword : String,
        @Query("radius") radius: Int,
    ) : ApiResponseDtoListMartDto
}