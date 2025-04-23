package project.graduation.crowd_sourcing.presentation.ui.screen.request.request

data class MartInfo(
    val name: String,
    val latitude: Double,
    val longitude: Double
)

val dummyMartList = listOf(
    MartInfo("상암 홈플러스", 37.579617, 126.890867),
    MartInfo("마포 이마트", 37.556345, 126.906219),
    MartInfo("은평 롯데마트", 37.601000, 126.929000)
)