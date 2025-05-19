package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.domain.repository.WorkerRepository
import javax.inject.Inject

class WorkerUseCase @Inject constructor(
    private val workerRepository: WorkerRepository
) {

}