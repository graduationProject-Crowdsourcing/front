package project.graduation.crowd_sourcing.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import project.graduation.crowd_sourcing.data.mapper.statistics.toEntity
import project.graduation.crowd_sourcing.data.service.StatisticsService
import project.graduation.crowd_sourcing.domain.model.entity.statistics.DetailEntity
import project.graduation.crowd_sourcing.domain.model.entity.statistics.MartListPriceEntity
import project.graduation.crowd_sourcing.domain.model.entity.statistics.ItemMinMaxPriceEntity
import project.graduation.crowd_sourcing.domain.model.entity.statistics.ItemListPriceEntity
import project.graduation.crowd_sourcing.domain.model.entity.statistics.MartMinMaxPriceEntity
import project.graduation.crowd_sourcing.domain.repository.StatisticsRepository
import javax.inject.Inject

class StatisticsRepositoryImpl @Inject constructor(
    private val statisticsService: StatisticsService
) : StatisticsRepository {
    override suspend fun getMartNames(id: Int): Result<List<String>> {
        return try {
            Result.success(statisticsService.getMartNames(id).map { it.martName })
        }catch (e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun getMinPriceMart(
        region: String,
        category: String
    ): Result<MartMinMaxPriceEntity> {
        return try {
            val response = statisticsService.getMinPriceMart(region, category).toEntity()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMaxPriceMart(
        region: String,
        category: String
    ): Result<MartMinMaxPriceEntity> {
        return try {
            val response = statisticsService.getMaxPriceMart(region, category).toEntity()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMinPriceItem(
        region: String,
        category: String
    ): Result<ItemMinMaxPriceEntity> {
        return try {
            val response = statisticsService.getMinPriceItem(region, category).toEntity()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMaxPriceItem(
        region: String,
        category: String
    ): Result<ItemMinMaxPriceEntity> {
        return try {
            val response = statisticsService.getMaxPriceItem(region, category).toEntity()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMartList(
        martNames: Array<String>,
        category: String
    ): Result<List<MartListPriceEntity>> {
        return try {
            val response = statisticsService.getMartList(martNames, category).map { it.toEntity() }
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getItemList(
        martNames: Array<String>,
        category: String
    ): Result<List<ItemListPriceEntity>> {
        return try {
            val response = statisticsService.getItemList(martNames, category).map { it.toEntity() }
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getCommissionDetail(id: Int): Result<DetailEntity> {
        return try {
            val response = statisticsService.getDetail(id).toEntity()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}