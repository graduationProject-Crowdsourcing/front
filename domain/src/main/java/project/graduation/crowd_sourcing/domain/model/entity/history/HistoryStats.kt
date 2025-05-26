package project.graduation.crowd_sourcing.domain.model.entity.history

import project.graduation.crowd_sourcing.data.response.worker.WorkHistoryEntity
import project.graduation.crowd_sourcing.domain.model.Category
import project.graduation.crowd_sourcing.domain.model.Region

data class HistoryStats(
    val hour:Int,
    val point: Int,
    val completed:Int,
    val completedList: List<WorkHistoryEntity>,
    val currentList: List<WorkHistoryEntity>,
    val mostRegion: Region,
    val mostCategory: Category,
)