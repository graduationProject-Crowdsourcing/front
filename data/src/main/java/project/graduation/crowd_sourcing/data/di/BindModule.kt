package project.graduation.crowd_sourcing.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import project.graduation.crowd_sourcing.data.repository.LoginRepositoryImpl
import project.graduation.crowd_sourcing.data.repository.MyRepositoryImpl
import project.graduation.crowd_sourcing.data.repository.StatisticsRepositoryImpl
import project.graduation.crowd_sourcing.data.repository.UserPointRepositoryImpl
import project.graduation.crowd_sourcing.domain.model.entity.userpoint.UserPointHistoryEntity
import project.graduation.crowd_sourcing.domain.repository.LoginRepository
import project.graduation.crowd_sourcing.domain.repository.MyRepository
import project.graduation.crowd_sourcing.domain.repository.StatisticsRepository
import project.graduation.crowd_sourcing.domain.repository.UserPointRepository

@InstallIn(SingletonComponent::class)
@Module
abstract class BindModule {
    @Binds
    abstract fun bindLoginRepository(
        repository: LoginRepositoryImpl
    ): LoginRepository

    @Binds
    abstract fun bindMyRepository(
        repository: MyRepositoryImpl
    ): MyRepository

    @Binds
    abstract fun bindUserPointRepository(
        repository: UserPointRepositoryImpl
    ): UserPointRepository

    @Binds
    abstract fun bindStatisticsRepository(
        repository: StatisticsRepositoryImpl
    ): StatisticsRepository
}
