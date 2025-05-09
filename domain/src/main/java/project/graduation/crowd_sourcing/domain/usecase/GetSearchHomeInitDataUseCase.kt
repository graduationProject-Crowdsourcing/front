package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.domain.model.entity.SearchHome
import project.graduation.crowd_sourcing.domain.repository.SearchRepository
import javax.inject.Inject

class GetSearchHomeInitDataUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(): SearchHome {
        return repository.getSearchHomeInitData()
    }
} 