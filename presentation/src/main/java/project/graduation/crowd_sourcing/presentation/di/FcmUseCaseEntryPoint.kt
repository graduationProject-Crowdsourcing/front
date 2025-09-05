package project.graduation.crowd_sourcing.presentation.di


import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import project.graduation.crowd_sourcing.domain.usecase.PostAcceptUseCase
import project.graduation.crowd_sourcing.domain.usecase.PostRejectWorkUseCase

@EntryPoint
@InstallIn(SingletonComponent::class)
interface FcmUseCaseEntryPoint {
    fun postAcceptUseCase(): PostAcceptUseCase
    fun postRejectWorkUseCase(): PostRejectWorkUseCase
}