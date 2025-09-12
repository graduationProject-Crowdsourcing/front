package project.graduation.crowd_sourcing.domain.usecase

import kotlinx.coroutines.flow.Flow
import project.graduation.crowd_sourcing.domain.local.TokenManager
import project.graduation.crowd_sourcing.domain.model.Noti
import project.graduation.crowd_sourcing.domain.repository.FcmRepository
import project.graduation.crowd_sourcing.domain.repository.NotiRepository
import javax.inject.Inject

class NotiUseCase @Inject constructor(
    private val notiRepository: NotiRepository,
    private val fcmRepository: FcmRepository,
    private val tokenManager: TokenManager
){
    suspend fun getAllNotes(): Flow<List<Noti>> = notiRepository.getAllNotes()
    suspend fun insertNote(note: Noti){
        notiRepository.insertNote(note)
    }
    suspend fun deleteNote(note: Noti){
        notiRepository.deleteNote(note)
    }

    suspend fun accept(workId: Int): Result<Unit> {
        return fcmRepository.postAccept(workId = workId, memberId = tokenManager.getUserId())
    }

    suspend fun reject(workId: Int):  Result<Unit> {
        return fcmRepository.postRejectWork(workId = workId, memberId = tokenManager.getUserId())
    }
}