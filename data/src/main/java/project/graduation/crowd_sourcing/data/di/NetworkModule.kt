package project.graduation.crowd_sourcing.data.di

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import project.graduation.crowd_sourcing.domain.local.TokenManager
import project.graduation.crowd_sourcing.data.network.AuthorizationInterceptor
import project.graduation.crowd_sourcing.data.network.TokenAuthenticator
import project.graduation.crowd_sourcing.data.service.LocationService
import project.graduation.crowd_sourcing.data.service.LoginService
import project.graduation.crowd_sourcing.data.service.MartSearchService
import project.graduation.crowd_sourcing.data.service.RequesterService
import project.graduation.crowd_sourcing.data.service.SearchService
import project.graduation.crowd_sourcing.data.service.MyService
import project.graduation.crowd_sourcing.data.service.StatisticsService
import project.graduation.crowd_sourcing.data.service.UserPointService
import project.graduation.crowd_sourcing.data.service.WorkService
import project.graduation.crowd_sourcing.data.service.WorkerService
import project.graduation.crowd_sourcing.data.service.alarm.AlarmService
import project.graduation.crowd_sourcing.data.service.alarm.FcmService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {
    @Provides
    fun provideAuthorizationInterceptor(
        tokenManager: TokenManager,
    ): AuthorizationInterceptor {
        return AuthorizationInterceptor(tokenManager)
    }


    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                android.util.Log.d("API_LOG", message)
            }
        }).apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .registerTypeAdapter(
                LocalDateTime::class.java,
                object : JsonDeserializer<LocalDateTime> {
                    override fun deserialize(
                        json: JsonElement,
                        typeOfT: Type,
                        context: JsonDeserializationContext
                    ): LocalDateTime {
                        val raw = json.asString
                        // 소수점 이하(.SSS...)를 잘라냄 → 초까지만 남김
                        val trimmed = raw.substringBefore(".")
                        return try {
                            LocalDateTime.parse(trimmed, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
                        } catch (e: Exception) {
                            e.printStackTrace()
                            LocalDateTime.now()
                        }
                    }
                }
            )
            .create()
    }



    @Provides
    fun provideOkHttpClient(
        authorizationInterceptor: AuthorizationInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        tokenAuthenticator: TokenAuthenticator
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authorizationInterceptor)
            .addInterceptor(loggingInterceptor)
            .authenticator(tokenAuthenticator)
            .build()
    }

    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://crowdsourcing.pe.kr/") // http://52.78.15.153:8112/
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    private inline fun <reified T> provideService(retrofit: Retrofit): T =
        retrofit.create(T::class.java)


    @Provides fun provideLoginService(retrofit: Retrofit): LoginService =
        provideService(retrofit)

    @Provides fun provideSearchService(retrofit: Retrofit): SearchService =
        provideService(retrofit)

    @Provides fun provideMartSearchService(retrofit: Retrofit): MartSearchService =
        provideService(retrofit)

    @Provides fun provideRequesterService(retrofit: Retrofit): RequesterService =
        provideService(retrofit)

    @Provides fun provideMyService(retrofit: Retrofit): MyService =
        provideService(retrofit)

    @Provides fun provideUserPointService(retrofit: Retrofit): UserPointService =
        provideService(retrofit)

    @Provides fun provideStatisticsService(retrofit: Retrofit): StatisticsService =
        provideService(retrofit)

    @Provides fun provideWorkService(retrofit: Retrofit): WorkService =
        provideService(retrofit)

    @Provides fun provideWorkerService(retrofit: Retrofit): WorkerService =
        provideService(retrofit)

    @Provides fun provideFcmService(retrofit: Retrofit): FcmService =
        provideService(retrofit)

    @Provides fun provideAlarmService(retrofit: Retrofit): AlarmService =
        provideService(retrofit)

    @Provides fun provideLocationService(retrofit: Retrofit): LocationService =
        provideService(retrofit)


    @Provides
    @Named("no-auth")
    fun provideNoAuthOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Named("no-auth")
    fun provideNoAuthRetrofit(
        @Named("no-auth") okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://52.78.15.153:8112/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Named("no-auth")
    fun provideNoAuthLoginService(
        @Named("no-auth") retrofit: Retrofit
    ): LoginService = provideService(retrofit)
}