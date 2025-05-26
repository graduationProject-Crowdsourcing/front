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

    suspend fun getMart(region: Region, category: Category): Result<List<MartListPriceEntity>> {
        return try {
            repository.getMartList(region.name, category.name)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getItem(region: Region, category: Category): Result<List<ItemListPriceEntity>> {
        return try {
            repository.getItemList(region.name, category.name)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDetail(id: Int): Result<DetailEntity>{
        return try{
            repository.getCommissionDetail(id)
        }catch (e:Exception){
            Result.failure(e)
        }
    }
}