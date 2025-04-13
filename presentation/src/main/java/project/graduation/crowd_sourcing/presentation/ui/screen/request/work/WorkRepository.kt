package project.graduation.crowd_sourcing.presentation.ui.screen.request.work

object WorkRepository {
    val workList = listOf(
        Work("1", "딸기 한 팩 가격", "수색 이마트", 15),
        Work("2", "진라면 한 묶음 가격", "상암 홈플러스", 10),
        Work("3", "신라면 투움바 한 봉지", "은평 롯데몰", 20),
        Work("4", "양배추 한 통", "성산 하나로마트", 10),
        Work("5", "삼다수 12병 묶음", "상암 홈플러스", 10)
    )

    fun getWorkById(id: String): Work? = workList.find { it.id == id }
}