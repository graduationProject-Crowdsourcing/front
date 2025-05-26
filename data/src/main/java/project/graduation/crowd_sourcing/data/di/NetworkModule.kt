package project.graduation.crowd_sourcing.data.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import project.graduation.crowd_sourcing.data.local.TokenManager
import project.graduation.crowd_sourcing.data.network.AuthorizationInterceptor
import project.graduation.crowd_sourcing.data.service.LoginService
import project.graduation.crowd_sourcing.data.service.MartSearchService
import project.graduation.crowd_sourcing.data.service.RequesterService
import project.graduation.crowd_sourcing.data.service.SearchService
import project.graduation.crowd_sourcing.data.service.MyService
import project.graduation.crowd_sourcing.data.service.StatisticsService
import project.graduation.crowd_sourcing.data.service.UserPointService
import project.graduation.crowd_sourcing.data.service.WorkService
import project.graduation.crowd_sourcing.data.service.WorkerService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date

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
    fun provideWorkService(
        retrofit: Retrofit
    ): WorkService {
        return retrofit.create(WorkService::class.java)
    }

    @Provides
    fun provideWorkerService(
        retrofit: Retrofit
    ): WorkerService {
        return retrofit.create(WorkerService::class.java)
    }
}