package project.graduation.crowd_sourcing.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import project.graduation.crowd_sourcing.data.network.AuthorizationInterceptor
import project.graduation.crowd_sourcing.data.service.LoginService
import project.graduation.crowd_sourcing.data.service.MartSearchService
import project.graduation.crowd_sourcing.data.service.RequesterService
import project.graduation.crowd_sourcing.data.service.SearchService
import project.graduation.crowd_sourcing.data.service.MyService
import project.graduation.crowd_sourcing.data.service.StatisticsService
import project.graduation.crowd_sourcing.data.service.UserPointService
import project.graduation.crowd_sourcing.data.service.WorkerService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

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
    fun provideGson(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create()
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
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://52.78.15.153:8112/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun provideLoginService(
        retrofit: Retrofit
    ): LoginService {
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

    @Provides
    fun provideRequesterService(
        retrofit: Retrofit
    ): RequesterService {
        return retrofit.create(RequesterService::class.java)
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

    @Provides
    fun provideWorkerService(
        retrofit: Retrofit
    ): WorkerService {
        return retrofit.create(WorkerService::class.java)
    }
}