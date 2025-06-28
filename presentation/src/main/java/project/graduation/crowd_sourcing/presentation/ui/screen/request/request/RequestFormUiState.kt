package project.graduation.crowd_sourcing.presentation.ui.screen.request.request

data class RequestFormUiState(
    val sigungu: String = "",         // 지역구 (사용자 입력)
    val maxPeople: String = "",       // 최대 인원
    val pointPerPerson: String = "",  // 1인당 포인트
    val item: String = "",            // 품목
    val selectedCategory: String = "",   // 사용자가 선택한 카테고리
    val expirationDate: String = ""   // 의뢰 마감일 (yyyy-MM-dd HH:mm 형식)
)