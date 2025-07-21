package project.graduation.crowd_sourcing.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import project.graduation.crowd_sourcing.data.local.NotiDao
import project.graduation.crowd_sourcing.data.local.toDomain
import project.graduation.crowd_sourcing.data.local.toEntity
import project.graduation.crowd_sourcing.domain.model.Noti
import project.graduation.crowd_sourcing.domain.repository.NotiRepository
import javax.inject.Inject

class NotiRepositoryImpl @Inject constructor(private val dao: NotiDao) : NotiRepository {
    override suspend fun getAllNotes(): Flow<List<Noti>> =
        dao.getAllNotes().map { list -> list.map { it.toDomain() } }


    override suspend fun insertNote(note: Noti) {
        dao.insert(note.toEntity())
    }

    override suspend fun deleteNote(note: Noti) {
        dao.delete(note.toEntity())
    }
}
