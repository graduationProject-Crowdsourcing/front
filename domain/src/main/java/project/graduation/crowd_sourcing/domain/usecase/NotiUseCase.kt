package project.graduation.crowd_sourcing.domain.usecase

import kotlinx.coroutines.flow.Flow
import project.graduation.crowd_sourcing.domain.model.Noti
import project.graduation.crowd_sourcing.domain.repository.NotiRepository
import javax.inject.Inject

class NotiUseCase @Inject constructor(
    private val notiRepository: NotiRepository
){
    suspend fun getAllNotes(): Flow<List<Noti>> = notiRepository.getAllNotes()
    suspend fun insertNote(note: Noti){
        notiRepository.insertNote(note)
    }
    suspend fun deleteNote(note: Noti){
        notiRepository.deleteNote(note)
    }
}