//package project.graduation.crowd_sourcing.presentation.di
//
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.android.components.ActivityComponent
//import project.graduation.crowd_sourcing.domain.repository.LoginRepository
//import project.graduation.crowd_sourcing.domain.usecase.LoginUseCase
//
//@Module
//@InstallIn(ActivityComponent::class)
//object PresentationModule {
//
//    @Provides
//    fun provideLoginUseCase(repository: LoginRepository): LoginUseCase {
//        return LoginUseCase(repository)
//    }
//}
