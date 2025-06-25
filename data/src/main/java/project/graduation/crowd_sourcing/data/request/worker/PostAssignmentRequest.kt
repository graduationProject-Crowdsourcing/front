package project.graduation.crowd_sourcing.data.request.worker

data class PostAssignmentRequest(
    val item: String,
    val itemPrice: Int,
    val workDate: String, // ISO 8601 형식의 날짜 문자열
    val martName: String
)
