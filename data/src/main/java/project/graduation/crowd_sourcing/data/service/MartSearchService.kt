package project.graduation.crowd_sourcing.data.service

import project.graduation.crowd_sourcing.data.response.martsearch.ApiResponseDtoListMartDto
import project.graduation.crowd_sourcing.data.response.martsearch.ApiResponseDtoListMartWorkDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MartSearchService {
    @POST("/api/v1/marts/search/zipcode")
    suspend fun getSearchMartByZipcode(
        @Query("zipcode") zipcode: String,
        @Query("radius") radius: Int,
    ): ApiResponseDtoListMartDto

    @GET("/api/v1/marts/search/memberNearby")
    suspend fun getSearchMartByLocation(
        @Query("latitude") lat : Double,
        @Query("longitude") lng : Double,
        @Query("radiusKm") radius: Double,
    ): ApiResponseDtoListMartDto

    @GET("/api/v1/marts/search/search-by-name")
    suspend fun getSearchMartByKeyword(
        @Query("keyword") keyword : String,
        @Query("radiusKm") radius: Double,
    ) : ApiResponseDtoListMartDto

    @GET("/api/v1/marts/search/martlist")
    suspend fun getSearchWorkByMartName(
        @Query("martNames") martName : String
    ) : ApiResponseDtoListMartWorkDto

    @GET("/api/v1/marts/search")
    suspend fun getMartList(
        @Query("sigungu") sigungu: String
    ): Response<ApiResponseDtoListMartDto>
}