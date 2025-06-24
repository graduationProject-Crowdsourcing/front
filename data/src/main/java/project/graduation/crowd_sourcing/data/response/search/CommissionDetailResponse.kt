package project.graduation.crowd_sourcing.data.response.search

import android.os.Build
import androidx.annotation.RequiresApi
import project.graduation.crowd_sourcing.domain.model.entity.search.CommissionDetailEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class CommissionDetailDto(
    val commissionId: Int,
    val commission: String,
    val region: String,
    val martName: String,
    val category: String,
    val item: String,
    val commissionPoint: Int,
    val commissionCount: Int,
    val createdAt: String,
    val expirationDate: String
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun toDomain(): CommissionDetailEntity {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val createdAtDateTime = LocalDateTime.parse(createdAt, formatter)
        val expirationDateTime = LocalDateTime.parse(expirationDate, formatter)
        
        return CommissionDetailEntity(
            commissionId = commissionId,
            commission = commission,
            region = region,
            martName = martName,
            category = category,
            item = item,
            commissionPoint = commissionPoint,
            commissionCount = commissionCount,
            createdAt = createdAtDateTime,
            expirationDate = expirationDateTime
        )
    }
} 