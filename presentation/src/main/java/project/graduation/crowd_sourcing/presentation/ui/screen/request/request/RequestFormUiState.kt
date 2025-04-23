package project.graduation.crowd_sourcing.presentation.ui.screen.request.request

data class RequestFormUiState(
    val martName: String = "",
    val maxPeople: String = "",
    val pointPerPerson: String = "",
    val item: String = "",
    val dateTime: String = "",
    val martLat: Double? = null,
    val martLng: Double? = null
)

