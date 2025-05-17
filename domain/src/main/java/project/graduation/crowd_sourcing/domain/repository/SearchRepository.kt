package project.graduation.crowd_sourcing.domain.repository

import kotlinx.coroutines.flow.Flow
import project.graduation.crowd_sourcing.domain.model.entity.search.CommissionEntity
import project.graduation.crowd_sourcing.domain.model.entity.search.SearchHomeEntity

interface SearchRepository {
//    suspend fun searchCommissions(): Flow<List<Commission>>
    suspend fun searchCommissions(
        searchKeyword: String = "",
        region: String = "",
        category: String = "",
        sort: String = "latest",
        order: String = "desc"
    ): Flow<List<CommissionEntity>>
    
    suspend fun getSearchHomeInitData(): SearchHomeEntity
}
