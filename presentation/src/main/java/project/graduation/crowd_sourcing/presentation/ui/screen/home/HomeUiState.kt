package project.graduation.crowd_sourcing.presentation.ui.screen.home

data class HomeUiState(
    val searchQuery: String = "",
    val currentLocation: Location = Location(0.0, 0.0),
    val requests: List<Request> = emptyList()
)

data class Location(
    val latitude: Double,
    val longitude: Double
)

data class Request(
    val id: String,
    val title: String,
    val location: Location,
    val place: String,
    val reward: Int
) 