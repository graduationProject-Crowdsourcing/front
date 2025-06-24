package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.domain.repository.AlarmRepository
import javax.inject.Inject

class GetSendNotificationUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(latitude: Double, longitude: Double): Result<String> {
        return alarmRepository.getSendNotification(latitude, longitude)
    }
} 