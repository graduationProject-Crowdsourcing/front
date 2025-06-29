package project.graduation.crowd_sourcing.data.mapper.mart

import project.graduation.crowd_sourcing.data.response.martsearch.MartDto
import project.graduation.crowd_sourcing.domain.model.entity.martsearch.MartEntity

fun MartDto.toEntity(): MartEntity {
    return MartEntity(
        martId = martId,
        martName = martName,
        latitude = latitude,
        longitude = longitude,
        sido = sido,
        sigungu = sigungu,
        dong = dong,
        existCommission = existCommission
    )
}