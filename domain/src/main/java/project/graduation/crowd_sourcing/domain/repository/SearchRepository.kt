package project.graduation.crowd_sourcing.domain.repository

import kotlinx.coroutines.flow.Flow
import project.graduation.crowd_sourcing.domain.model.entity.search.Commission
import project.graduation.crowd_sourcing.domain.model.entity.search.SearchHome

interface SearchRepository {
//    suspend fun searchCommissions(): Flow<List<Commission>>
    suspend fun searchCommissions(
        searchKeyword: String = "",
        region: String = "",
        category: String = "",
        sort: String = "latest",
        order: String = "desc"
    ): Flow<List<Commission>>
    
    suspend fun getSearchHomeInitData(): SearchHome
}
