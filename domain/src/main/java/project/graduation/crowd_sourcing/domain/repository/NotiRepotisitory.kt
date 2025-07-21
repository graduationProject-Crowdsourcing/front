package project.graduation.crowd_sourcing.domain.repository

import kotlinx.coroutines.flow.Flow
import project.graduation.crowd_sourcing.domain.model.Noti

interface NotiRepository {
    suspend fun getAllNotes(): Flow<List<Noti>>
    suspend fun insertNote(note: Noti)
    suspend fun deleteNote(note: Noti)
}
