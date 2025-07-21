package project.graduation.crowd_sourcing.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import project.graduation.crowd_sourcing.data.local.AppDatabase
import javax.inject.Singleton
import project.graduation.crowd_sourcing.data.local.NotiDao

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideNotiDao(db: AppDatabase): NotiDao {
        return db.notiDao()
    }
}
