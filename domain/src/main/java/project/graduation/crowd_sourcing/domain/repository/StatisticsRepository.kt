package project.graduation.crowd_sourcing.domain.repository

import project.graduation.crowd_sourcing.domain.model.entity.statistics.DetailEntity
import project.graduation.crowd_sourcing.domain.model.entity.statistics.MartListPriceEntity
import project.graduation.crowd_sourcing.domain.model.entity.statistics.ItemMinMaxPriceEntity
import project.graduation.crowd_sourcing.domain.model.entity.statistics.ItemListPriceEntity
import project.graduation.crowd_sourcing.domain.model.entity.statistics.MartMinMaxPriceEntity
import retrofit2.http.GET
import retrofit2.http.Path

interface StatisticsRepository {
    suspend fun getMartNames(id: Int): Result<List<String>>

    suspend fun getMinPriceMart(region: String, category: String): Result<MartMinMaxPriceEntity>

    suspend fun getMaxPriceMart(region: String, category: String): Result<MartMinMaxPriceEntity>

    suspend fun getMinPriceItem(region: String, category: String): Result<ItemMinMaxPriceEntity>

    suspend fun getMaxPriceItem(region: String, category: String): Result<ItemMinMaxPriceEntity>

    suspend fun getMartList(martNames: Array<String>, category: String): Result<List<MartListPriceEntity>>

    suspend fun getItemList(martNames: Array<String>, category: String): Result<List<ItemListPriceEntity>>

    suspend fun getCommissionDetail(id: Int): Result<DetailEntity>
}