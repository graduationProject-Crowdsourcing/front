package project.graduation.crowd_sourcing.data.service

import project.graduation.crowd_sourcing.data.response.search.CommissionDetailDto
import project.graduation.crowd_sourcing.data.response.search.CommissionDto
import project.graduation.crowd_sourcing.data.response.search.SearchCommissionResponse
import project.graduation.crowd_sourcing.data.response.search.SearchHomeDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchService {
    @GET("/api/v1/search")
    suspend fun getSearchCommission(
        @Query("searchKeyword") searchKeyword: String = "",
        @Query("region") region: String = "ALL",
        @Query("category") category: String = "ALL",
        @Query("sort") sort: String = "createdAt",
        @Query("order") order: String = "asc"
    ): SearchCommissionResponse<List<CommissionDto>>

    @GET("/api/v1/search/init")
    suspend fun getSearchHomeInit(): SearchCommissionResponse<SearchHomeDto>

    @GET("/api/v1/search/detail/{commissionId}")
    suspend fun getCommissionDetail(
        @Path("commissionId") commissionId: Int
    ): CommissionDetailDto
}