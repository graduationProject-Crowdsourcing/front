package project.graduation.crowd_sourcing.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import project.graduation.crowd_sourcing.data.repository.LoginRepositoryImpl
import project.graduation.crowd_sourcing.data.repository.SearchRepositoryImpl
import project.graduation.crowd_sourcing.domain.repository.LoginRepository
import project.graduation.crowd_sourcing.domain.repository.SearchRepository
import javax.inject.Singleton

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
}
