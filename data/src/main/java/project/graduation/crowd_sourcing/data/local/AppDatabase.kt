package project.graduation.crowd_sourcing.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [NotiEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun notiDao(): NotiDao
}
