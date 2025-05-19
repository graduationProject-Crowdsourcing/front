package project.graduation.crowd_sourcing.domain.usecase

import project.graduation.crowd_sourcing.domain.repository.WorkRepository
import java.io.File
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val repository: WorkRepository
) {
    suspend operator fun invoke(
        username: String,
        directoryPath: String,
        imageFile: File
    ): Result<String> {
        return repository.uploadImage(username, directoryPath, imageFile)
    }
}