package project.graduation.crowd_sourcing.domain.repository

import android.net.Uri

interface WorkRepository {
    suspend fun uploadImage(
        username: String,
        directoryPath: String,
        uri: Uri
    ): Result<Unit>

    suspend fun requestOcr(
        fileName: String,
        commissionId: String
    ): Result<String>
}