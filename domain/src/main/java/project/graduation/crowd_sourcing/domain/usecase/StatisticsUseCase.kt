package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.domain.repository.StatisticsRepository
import javax.inject.Inject

class StatisticsUseCase @Inject constructor(
    private val repository: StatisticsRepository
) {

}