package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.domain.model.entity.statistics.DetailEntity
import project.graduation.crowd_sourcing.domain.model.entity.statistics.ItemListPriceEntity
import project.graduation.crowd_sourcing.domain.model.entity.statistics.MartListPriceEntity
import project.graduation.crowd_sourcing.domain.repository.StatisticsRepository
import javax.inject.Inject

class StatisticsUseCase @Inject constructor(
    private val repository: StatisticsRepository
) {

    suspend fun getMart(idList: List<Int>, category: String): Result<List<MartListPriceEntity>> {
        return try {
            val martNames = mutableListOf<String>()
            idList.forEach {
                val names = repository.getMartNames(it).getOrThrow()
                martNames.addAll(names)
            }

            repository.getMartList(
                martNames.toTypedArray(),
                category
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getItem(idList: List<Int>, category: String): Result<List<ItemListPriceEntity>> {
        return try {
            val martNames = mutableListOf<String>()
            idList.forEach {
                val names = repository.getMartNames(it).getOrThrow()
                martNames.addAll(names)
            }

            repository.getItemList(
                martNames.toTypedArray(),
                category
            )
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