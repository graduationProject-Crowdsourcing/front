package project.graduation.crowd_sourcing.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NotiDao {
    @Query("SELECT * FROM note_table")
    fun getAllNotes(): Flow<List<NotiEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: NotiEntity)

    @Delete
    suspend fun delete(note: NotiEntity)
}
