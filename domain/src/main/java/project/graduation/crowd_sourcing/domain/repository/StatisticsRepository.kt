package project.graduation.crowd_sourcing.domain.repository

import project.graduation.crowd_sourcing.domain.model.entity.statistics.DetailEntity
import project.graduation.crowd_sourcing.domain.model.entity.statistics.MartListPriceEntity
import project.graduation.crowd_sourcing.domain.model.entity.statistics.ItemMinMaxPriceEntity
import project.graduation.crowd_sourcing.domain.model.entity.statistics.ItemListPriceEntity
import project.graduation.crowd_sourcing.domain.model.entity.statistics.MartMinMaxPriceEntity

interface StatisticsRepository {
    suspend fun getMinPriceMart(region: String, category: String): Result<MartMinMaxPriceEntity>

    suspend fun getMaxPriceMart(region: String, category: String): Result<MartMinMaxPriceEntity>

    suspend fun getMinPriceItem(region: String, category: String): Result<ItemMinMaxPriceEntity>

    suspend fun getMaxPriceItem(region: String, category: String): Result<ItemMinMaxPriceEntity>

    suspend fun getMartList(region: String, category: String): Result<List<MartListPriceEntity>>

    suspend fun getItemList(region: String, category: String): Result<List<ItemListPriceEntity>>

    suspend fun getCommissionDetail(id: Int): Result<DetailEntity>
}