package project.graduation.crowd_sourcing.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import project.graduation.crowd_sourcing.domain.local.TokenManager
import project.graduation.crowd_sourcing.domain.repository.FcmRepository
import project.graduation.crowd_sourcing.domain.repository.LocationRepository
import project.graduation.crowd_sourcing.domain.repository.LoginRepository
import project.graduation.crowd_sourcing.domain.repository.MyRepository
import project.graduation.crowd_sourcing.domain.repository.RequesterRepository
import project.graduation.crowd_sourcing.domain.repository.StatisticsRepository
import project.graduation.crowd_sourcing.domain.repository.UserPointRepository
import project.graduation.crowd_sourcing.domain.repository.WorkerRepository
import project.graduation.crowd_sourcing.domain.usecase.AlarmUseCase
import project.graduation.crowd_sourcing.domain.usecase.HistoryUseCase
import project.graduation.crowd_sourcing.domain.usecase.MemberUseCase
import project.graduation.crowd_sourcing.domain.usecase.MyUseCase
import project.graduation.crowd_sourcing.domain.usecase.RequesterUseCase
import project.graduation.crowd_sourcing.domain.usecase.StatisticsUseCase
import project.graduation.crowd_sourcing.domain.usecase.WorkerUseCase

@Module
@InstallIn(ActivityComponent::class)
object PresentationModule {

    @Provides
    fun provideMemberUseCase(
        loginRepository: LoginRepository,
        fcmRepository: FcmRepository,
        tokenManager: TokenManager
    ): MemberUseCase = MemberUseCase(loginRepository, fcmRepository,tokenManager)

    @Provides
    fun provideMyUseCase(repository: MyRepository): MyUseCase {
        return MyUseCase(repository)
    }

    @Provides
    fun provideHistoryUseCase(
        pointRepository: UserPointRepository,
        requesterRepository: RequesterRepository,
        workerRepository: WorkerRepository,
        tokenManager: TokenManager
    ): HistoryUseCase {
        return HistoryUseCase(
            pointRepository,
            requesterRepository = requesterRepository,
            workerRepository = workerRepository,
            tokenManager = tokenManager
        )
    }

    @Provides
    fun provideStatisticsUseCase(repository: StatisticsRepository): StatisticsUseCase {
        return StatisticsUseCase(repository)
    }

    @Provides
    fun provideWorkerUseCase(repository: WorkerRepository, tokenManager: TokenManager): WorkerUseCase {
        return WorkerUseCase(repository, tokenManager)
    }

    @Provides
    fun providesAlarmUseCase(fcmRepository: FcmRepository, locationRepository: LocationRepository, tokenManager: TokenManager): AlarmUseCase{
        return AlarmUseCase(fcmRepository, locationRepository, tokenManager)
    }
}
