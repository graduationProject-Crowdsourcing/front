package project.graduation.crowd_sourcing.domain.usecase

import kotlinx.coroutines.flow.Flow
import project.graduation.crowd_sourcing.domain.model.entity.Commission
import project.graduation.crowd_sourcing.domain.repository.SearchRepository
import javax.inject.Inject

class SearchCommissionUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(
        searchKeyword: String = "",
        region: String = "",
        category: String = "",
        sort: String = "latest",
        order: String = "desc"
    ): Flow<List<Commission>> {
        return repository.searchCommissions(
            searchKeyword = searchKeyword,
            region = region,
            category = category,
            sort = sort,
            order = order
        )
    }
}