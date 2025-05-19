package project.graduation.crowd_sourcing.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import project.graduation.crowd_sourcing.data.repository.LoginRepositoryImpl
import project.graduation.crowd_sourcing.data.repository.MartSearchRepositoryImpl
import project.graduation.crowd_sourcing.data.repository.RequesterRepositoryImpl
import project.graduation.crowd_sourcing.data.repository.SearchRepositoryImpl
import project.graduation.crowd_sourcing.domain.repository.LoginRepository
import project.graduation.crowd_sourcing.domain.repository.MartSearchRepository
import project.graduation.crowd_sourcing.domain.repository.RequesterRepository
import project.graduation.crowd_sourcing.domain.repository.SearchRepository
import javax.inject.Singleton
import project.graduation.crowd_sourcing.data.repository.MyRepositoryImpl
import project.graduation.crowd_sourcing.data.repository.StatisticsRepositoryImpl
import project.graduation.crowd_sourcing.data.repository.UserPointRepositoryImpl
import project.graduation.crowd_sourcing.data.repository.WorkerRepositoryImpl
import project.graduation.crowd_sourcing.domain.repository.MyRepository
import project.graduation.crowd_sourcing.domain.repository.StatisticsRepository
import project.graduation.crowd_sourcing.domain.repository.UserPointRepository
import project.graduation.crowd_sourcing.domain.repository.WorkerRepository

@InstallIn(SingletonComponent::class)
@Module
abstract class BindModule {
    @Binds
    abstract fun bindLoginRepository(
        repository: LoginRepositoryImpl
    ): LoginRepository

    @Binds
    @Singleton
    abstract fun bindCommissionRepository(
        repository: SearchRepositoryImpl
    ): SearchRepository

    @Binds
    @Singleton
    abstract fun bindMartSearchRepository(
        martSearchRepositoryImpl: MartSearchRepositoryImpl
    ): MartSearchRepository
    
    @Binds
    @Singleton
    abstract fun bindRequesterRepository(
        requesterRepositoryImpl: RequesterRepositoryImpl
    ): RequesterRepository

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

    @Binds
    abstract fun bindWorkerRepository(
        repository: WorkerRepositoryImpl
    ): WorkerRepository
}
