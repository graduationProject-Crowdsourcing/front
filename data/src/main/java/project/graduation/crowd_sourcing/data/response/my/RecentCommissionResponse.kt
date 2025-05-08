package project.graduation.crowd_sourcing.data.response.my

data class RecentCommissionResponse (
    val id: Long,
    val commission: String,
    val region: String,
    val category: String,
    val commissionDate: String,
    val commissionPoint: Int
)