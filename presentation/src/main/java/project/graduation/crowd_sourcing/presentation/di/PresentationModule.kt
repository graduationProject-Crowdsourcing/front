package project.graduation.crowd_sourcing.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import project.graduation.crowd_sourcing.domain.repository.LoginRepository
import project.graduation.crowd_sourcing.domain.repository.MyRepository
import project.graduation.crowd_sourcing.domain.repository.StatisticsRepository
import project.graduation.crowd_sourcing.domain.repository.UserPointRepository
import project.graduation.crowd_sourcing.domain.repository.WorkerRepository
import project.graduation.crowd_sourcing.domain.usecase.HistoryUseCase
import project.graduation.crowd_sourcing.domain.usecase.MemberUseCase
import project.graduation.crowd_sourcing.domain.usecase.MyUseCase
import project.graduation.crowd_sourcing.domain.usecase.StatisticsUseCase
import project.graduation.crowd_sourcing.domain.usecase.WorkerUseCase

@Module
@InstallIn(ActivityComponent::class)
object PresentationModule {

    @Provides
    fun provideMemberUseCase(
        loginRepository: LoginRepository
    ): MemberUseCase = MemberUseCase(loginRepository)

    @Provides
    fun provideMyUseCase(repository: MyRepository): MyUseCase {
        return MyUseCase(repository)
    }

    @Provides
    fun provideHistoryUseCase(pointRepository: UserPointRepository): HistoryUseCase {
        return HistoryUseCase(pointRepository)
    }

    @Provides
    fun provideStatisticsUseCase(repository: StatisticsRepository): StatisticsUseCase {
        return StatisticsUseCase(repository)
    }

    @Provides
    fun provideWorkerUseCase(repository: WorkerRepository): WorkerUseCase {
        return WorkerUseCase(repository)
    }
}
