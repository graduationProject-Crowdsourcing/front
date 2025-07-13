package project.graduation.crowd_sourcing.domain.usecase

import android.net.Uri
import kotlinx.coroutines.delay
import project.graduation.crowd_sourcing.domain.local.TokenManager
import project.graduation.crowd_sourcing.domain.repository.WorkRepository
import retrofit2.HttpException
import javax.inject.Inject

class OcrRequestUseCase @Inject constructor(
    private val repository: WorkRepository,
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(
        uri: Uri,
        commissionId: String
    ): Result<List<String>> {
        return try {
            tokenManager.getUserName()?.let{ username->
                val fileName = "${username}_${System.currentTimeMillis()}.jpg"
                val uploadResult = repository.uploadImage(username, fileName, uri)

                val savedKey = uploadResult.getOrNull()?.replaceFirst("images/", "")
                if(uploadResult.isSuccess && savedKey != null) {
                    repository.requestOcr(savedKey, "1")
                }else Result.failure(
                    IllegalStateException("업로드 실패: ${uploadResult.exceptionOrNull()?.message}")
                )
                
            } ?: Result.failure(IllegalStateException("유저 정보가 없음."))
        } catch (e:Exception){
            Result.failure(e)
        }
    }
}