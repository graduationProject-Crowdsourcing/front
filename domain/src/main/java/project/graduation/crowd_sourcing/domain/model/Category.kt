package project.graduation.crowd_sourcing.domain.model

enum class Category(val koreanName: String) {
    UNKNOWN(""),
    Ramen("라면");
    companion object {
        fun from(value: String): Category {
            return entries.firstOrNull { it.koreanName == value }
                ?: UNKNOWN
        }
    }
}
