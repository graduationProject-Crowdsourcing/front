package project.graduation.crowd_sourcing.data.service

import project.graduation.crowd_sourcing.data.response.statistics.DetailResponse
import project.graduation.crowd_sourcing.data.response.statistics.MartListPriceResponse
import project.graduation.crowd_sourcing.data.response.statistics.ItemMinMaxPriceResponse
import project.graduation.crowd_sourcing.data.response.statistics.ItemListPriceResponse
import project.graduation.crowd_sourcing.data.response.statistics.MartMinMaxPriceResponse
import project.graduation.crowd_sourcing.data.response.statistics.MartNameResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StatisticsService {
    @GET("/api/v1/statistics/{id}/mart-names")
    suspend fun getMartNames(
        @Path("id") id: Int
    ): List<MartNameResponse>

    @GET("/api/v1/statistics/statistics/region-category-min-mart")
    suspend fun getMinPriceMart(
        @Query("region") region: String,
        @Query("category") category: String
    ): MartMinMaxPriceResponse

    @GET("/api/v1/statistics/statistics/region-category-max-mart")
    suspend fun getMaxPriceMart(
        @Query("region") region: String,
        @Query("category") category: String
    ): MartMinMaxPriceResponse

    @GET("/api/v1/statistics/statistics/region-category-min-item")
    suspend fun getMinPriceItem(
        @Query("region") region: String,
        @Query("category") category: String
    ): ItemMinMaxPriceResponse

    @GET("/api/v1/statistics/statistics/region-category-max-item")
    suspend fun getMaxPriceItem(
        @Query("region") region: String,
        @Query("category") category: String
    ): ItemMinMaxPriceResponse

    @GET("/api/v1/statistics/statistics/mart-category-prices")
    suspend fun getMartList(
        @Query("martNames") martNames: Array<String>,
        @Query("category") category: String
    ): List<MartListPriceResponse>

    @GET("/api/v1/statistics/statistics/mart-category-item-prices")
    suspend fun getItemList(
        @Query("martNames") martNames: Array<String>,
        @Query("category") category: String
    ): List<ItemListPriceResponse>

    @GET("/api/v1/statistics/statistics/commission/{id}")
    suspend fun getDetail(
        @Path("id") id: Int
    ): DetailResponse
}