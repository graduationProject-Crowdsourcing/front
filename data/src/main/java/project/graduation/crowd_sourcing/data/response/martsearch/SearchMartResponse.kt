package project.graduation.crowd_sourcing.data.response.martsearch

import project.graduation.crowd_sourcing.domain.model.WorkStatus
import java.time.LocalDateTime

data class ApiResponseDtoListMartDto (
    val status: Int,
    val message: String,
    val data: List<MartDto>
)

data class MartDto(
    val martId: Int,
    val martName: String,
    val latitude: Double,
    val longitude: Double,
    val sido: String,
    val sigungu: String?,
    val dong: String?,
    val existCommission: Int
)

data class ApiResponseDtoListMartWorkDto(
    val status: Int,
    val message: String,
    val data: List<MartWorkDto>
)

data class MartWorkDto(
    val id : Int,
    val work : String,
    val workCount : Int,
    val workpoint : Int,
    val workhour : Int,
    val category: String,
    val item : String,
    val workDate : LocalDateTime,
    val itemPrice : Int,
    val workStatus: WorkStatus
)

