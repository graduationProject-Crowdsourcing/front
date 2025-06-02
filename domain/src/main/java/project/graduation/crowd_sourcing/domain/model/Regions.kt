package project.graduation.crowd_sourcing.domain.model

enum class Region(val koreanName: String) {
    UNKNOWN(""),
    DONGDAEMUN("동대문구"),
    DOBONGU("도봉구");
    companion object {
        fun from(value: String): Region {
            return entries.firstOrNull { it.koreanName == value }
                ?: UNKNOWN
        }
    }
}

