package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.domain.model.Category
import project.graduation.crowd_sourcing.domain.model.Region
import project.graduation.crowd_sourcing.domain.model.entity.statistics.DetailEntity
import project.graduation.crowd_sourcing.domain.model.entity.statistics.ItemListPriceEntity
import project.graduation.crowd_sourcing.domain.model.entity.statistics.MartListPriceEntity
import project.graduation.crowd_sourcing.domain.repository.StatisticsRepository
import javax.inject.Inject

class StatisticsUseCase @Inject constructor(
    private val repository: StatisticsRepository
) {

    suspend fun getMart(id: Int, category: String): Result<List<MartListPriceEntity>> {
        return try {
            val martNames = repository.getMartNames(id)

            repository.getMartList(
                martNames.getOrThrow().toTypedArray(),
                category
            )

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getItem(id: Int, category: String): Result<List<ItemListPriceEntity>> {
        return try {
            val martNames = repository.getMartNames(id)

            repository.getItemList(martNames.getOrThrow().toTypedArray(), category)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDetail(id: Int): Result<DetailEntity> {
        return try {
            repository.getCommissionDetail(id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}