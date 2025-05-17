package project.graduation.crowd_sourcing.domain.model.entity.search

data class SearchHome(
    val regionList: List<String>,
    val categoryList: List<String>,
    val recentKeywords: List<String>,
    val recommendedKeywords: List<String>
)
