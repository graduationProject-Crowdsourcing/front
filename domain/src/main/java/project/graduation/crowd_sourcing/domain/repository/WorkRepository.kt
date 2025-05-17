package project.graduation.crowd_sourcing.domain.repository

import java.io.File

interface WorkRepository {
    suspend fun uploadImage(
        username: String,
        directoryPath: String,
        imageFile: File
    ): Result<String>
}
