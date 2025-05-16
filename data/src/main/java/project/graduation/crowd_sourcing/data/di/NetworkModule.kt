package project.graduation.crowd_sourcing.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import project.graduation.crowd_sourcing.data.local.TokenManager
import project.graduation.crowd_sourcing.data.network.AuthorizationInterceptor
import project.graduation.crowd_sourcing.data.service.LoginService
import project.graduation.crowd_sourcing.data.service.MyService
import project.graduation.crowd_sourcing.data.service.StatisticsService
import project.graduation.crowd_sourcing.data.service.UserPointService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Provides
    fun provideAuthorizationInterceptor(
        tokenManager: TokenManager
    ): AuthorizationInterceptor {
        return AuthorizationInterceptor(tokenManager)
    }


    @Provides
    fun provideOkHttpClient(
        authorizationInterceptor: AuthorizationInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authorizationInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://52.78.15.153:8080")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideLoginService(
        retrofit: Retrofit
    ): LoginService {
        return retrofit.create(LoginService::class.java)
    }

    @Provides
    fun provideMyService(
        retrofit: Retrofit
    ): MyService {
        return retrofit.create(MyService::class.java)
    }

    @Provides
    fun provideUserPointService(
        retrofit: Retrofit
    ): UserPointService {
        return retrofit.create(UserPointService::class.java)
    }

    @Provides
    fun provideStatisticsService(
        retrofit: Retrofit
    ): StatisticsService {
        return retrofit.create(StatisticsService::class.java)
    }
}