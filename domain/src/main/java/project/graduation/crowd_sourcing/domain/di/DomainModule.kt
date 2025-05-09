package project.graduation.crowd_sourcing.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import project.graduation.crowd_sourcing.domain.repository.SearchRepository
import project.graduation.crowd_sourcing.domain.usecase.GetSearchHomeInitDataUseCase
import project.graduation.crowd_sourcing.domain.usecase.SearchCommissionUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    
    @Provides
    @Singleton
    fun provideSearchCommissionUseCase(repository: SearchRepository): SearchCommissionUseCase {
        return SearchCommissionUseCase(repository)
    }
    
    @Provides
    @Singleton
    fun provideGetSearchHomeInitDataUseCase(repository: SearchRepository): GetSearchHomeInitDataUseCase {
        return GetSearchHomeInitDataUseCase(repository)
    }
} 