package project.graduation.crowd_sourcing.data.response.my

data class RecentWorkResponse(
    val id: Long,
    val work: String,
    val region: String,
    val category: String,
    val item: String,
    val itemPrice: Int,
    val workDate: String
)