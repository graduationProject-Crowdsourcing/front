package project.graduation.crowd_sourcing.data.response.search

import android.os.Build
import androidx.annotation.RequiresApi
import project.graduation.crowd_sourcing.domain.model.entity.search.CommissionEntity
import project.graduation.crowd_sourcing.domain.model.entity.search.SearchHomeEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class SearchCommissionResponse<T> (
    val status: Int,
    val message: String,
    val data: T
)

data class CommissionDto(
    val commissionId: Int,
    val commission: String,
    val commissionpoint: Int,
    val deadline: String
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun toCommission(): CommissionEntity {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val deadlineDateTime = LocalDateTime.parse(deadline, formatter)
        
        return CommissionEntity(
            commissionId = commissionId,
            commission = commission,
            commissionpoint = commissionpoint,
            deadline = deadlineDateTime
        )
    }
}

data class SearchHomeDto(
    val regionList: List<String>,
    val categoryList: List<String>,
    val recentSearchDtoList: List<SearchDto>,
    val recommendSearchDtoList: List<SearchDto>?
){
    fun toDomain(): SearchHomeEntity {
        return SearchHomeEntity(
            regionList = regionList,
            categoryList = categoryList,
            recentKeywords = recentSearchDtoList.map { it.searchKeyword },
            recommendedKeywords = recommendSearchDtoList?.map { it.searchKeyword } ?: emptyList()
        )
    }
}

data class SearchDto(
    val searchKeyword: String
)