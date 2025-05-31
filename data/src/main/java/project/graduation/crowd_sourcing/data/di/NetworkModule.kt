package project.graduation.crowd_sourcing.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import project.graduation.crowd_sourcing.domain.local.TokenManager
import project.graduation.crowd_sourcing.data.network.AuthorizationInterceptor
import project.graduation.crowd_sourcing.data.network.TokenAuthenticator
import project.graduation.crowd_sourcing.data.service.LoginService
import project.graduation.crowd_sourcing.data.service.MartSearchService
import project.graduation.crowd_sourcing.data.service.RequesterService
import project.graduation.crowd_sourcing.data.service.SearchService
import project.graduation.crowd_sourcing.data.service.MyService
import project.graduation.crowd_sourcing.data.service.StatisticsService
import project.graduation.crowd_sourcing.data.service.UserPointService
import project.graduation.crowd_sourcing.data.service.WorkService
import project.graduation.crowd_sourcing.data.service.WorkerService
import project.graduation.crowd_sourcing.data.service.alarm.FcmService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .setLenient()
            .registerTypeAdapter(
                java.time.LocalDateTime::class.java,
                object : com.google.gson.JsonDeserializer<java.time.LocalDateTime> {
                    override fun deserialize(
                        json: com.google.gson.JsonElement,
                        typeOfT: java.lang.reflect.Type,
                        context: com.google.gson.JsonDeserializationContext
                    ): java.time.LocalDateTime {
                        try {
                            val dateString = json.asString
                            android.util.Log.d("DateConverter", "JSON 날짜 문자열: $dateString")
                            return java.time.LocalDateTime.parse(
                                dateString.replace("+00:00", "Z"),
                                java.time.format.DateTimeFormatter.ISO_DATE_TIME
                            )
                        } catch (e: Exception) {
                            android.util.Log.e("DateConverter", "날짜 변환 오류: ${e.message}")
                            e.printStackTrace()
                            return java.time.LocalDateTime.now()
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
            .baseUrl("http://52.78.15.153:8112/")
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