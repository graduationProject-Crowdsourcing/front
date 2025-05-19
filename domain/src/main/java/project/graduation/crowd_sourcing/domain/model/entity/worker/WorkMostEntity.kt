package project.graduation.crowd_sourcing.data.response.worker

import project.graduation.crowd_sourcing.domain.model.Region

data class WorkMostEntity(
    val region: Region,
    val item: String,
    val dayOfWeek: String
)