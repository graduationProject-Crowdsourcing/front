package project.graduation.crowd_sourcing.domain.repository

import project.graduation.crowd_sourcing.domain.model.entity.search.CommissionEntity
import project.graduation.crowd_sourcing.domain.model.entity.search.SearchHomeEntity

interface SearchRepository {
    suspend fun searchCommissions(
        searchKeyword: String = "",
        region: String = "",
        category: String = "",
        sort: String = "latest",
        order: String = "desc"
    ): List<CommissionEntity>
    
    suspend fun getSearchHomeInitData(): SearchHomeEntity
}
