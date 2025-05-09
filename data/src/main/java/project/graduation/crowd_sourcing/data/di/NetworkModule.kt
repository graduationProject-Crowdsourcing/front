package project.graduation.crowd_sourcing.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import project.graduation.crowd_sourcing.data.network.AuthorizationInterceptor
import project.graduation.crowd_sourcing.data.service.LoginService
import project.graduation.crowd_sourcing.data.service.MartSearchService
import project.graduation.crowd_sourcing.data.service.SearchService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Provides
    fun provideAuthorizationInterceptor(): AuthorizationInterceptor {
        return AuthorizationInterceptor()
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun provideOkHttpClient(
        authorizationInterceptor: AuthorizationInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authorizationInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://52.78.15.153:8080/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideLoginService(
        retrofit: Retrofit
    ): LoginService{
        return retrofit.create(LoginService::class.java)
    }

    @Provides
    fun provideSearchService(
        retrofit: Retrofit
    ): SearchService{
        return retrofit.create(SearchService::class.java)
    }

    @Provides
    fun provideMartSearchService(
        retrofit: Retrofit
    ): MartSearchService {
        return retrofit.create(MartSearchService::class.java)
    }


}