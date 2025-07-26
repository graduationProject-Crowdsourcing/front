package project.graduation.crowd_sourcing.presentation.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import project.graduation.crowd_sourcing.domain.usecase.NotiUseCase

@EntryPoint
@InstallIn(SingletonComponent::class)
interface NotiUseCaseEntryPoint {
    fun notiUseCase(): NotiUseCase
}
