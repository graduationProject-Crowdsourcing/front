package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.domain.model.entity.search.CommissionDetailEntity
import project.graduation.crowd_sourcing.domain.repository.SearchRepository
import javax.inject.Inject

class GetCommissionDetailUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(commissionId: Int): CommissionDetailEntity {
        return searchRepository.getCommissionDetail(commissionId)
    }
} 