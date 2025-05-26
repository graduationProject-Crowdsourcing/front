package project.graduation.crowd_sourcing.domain.model

enum class Category(name: String) {
    UNKNOWN(""),
    Ramen("라면");
    companion object {
        fun from(value: String): Category {
            return entries.firstOrNull { it.name == value }
                ?: UNKNOWN
        }
    }
}
