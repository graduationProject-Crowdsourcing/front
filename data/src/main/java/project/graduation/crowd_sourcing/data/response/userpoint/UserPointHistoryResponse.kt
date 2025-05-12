package project.graduation.crowd_sourcing.data.response.userpoint

data class UserPointHistoryResponse(
    val type: String,
    val region: String,
    val item: String,
    val point: Int,
    val date: String
)