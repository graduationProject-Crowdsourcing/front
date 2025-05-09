package project.graduation.crowd_sourcing.domain.model.entity

data class SearchHome(
    val regionList: List<String>,
    val categoryList: List<String>,
    val recentKeywords: List<String>,
    val recommendedKeywords: List<String>
)
