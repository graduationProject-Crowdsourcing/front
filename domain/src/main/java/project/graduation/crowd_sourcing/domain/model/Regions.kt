package project.graduation.crowd_sourcing.domain.model

enum class Region(name: String) {
    UNKNOWN(""),
    DONGDAEMUN("동대문구");
    companion object {
        fun from(value: String): Region {
            return entries.firstOrNull { it.name == value }
                ?: UNKNOWN
        }
    }
}

