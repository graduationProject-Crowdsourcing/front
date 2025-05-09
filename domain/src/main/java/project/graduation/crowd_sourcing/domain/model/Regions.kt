package project.graduation.crowd_sourcing.domain.model

enum class Region(name: String) {
    UNKNOWN(""),
    SEOUL("서울"),
    BUSAN("부산");

    companion object {
        fun from(value: String): Region {
            return entries.firstOrNull { it.name == value }
                ?: UNKNOWN
        }
    }
}

