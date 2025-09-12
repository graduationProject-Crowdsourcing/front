package project.graduation.crowd_sourcing.domain.model.entity.history

import project.graduation.crowd_sourcing.data.response.worker.WorkHistoryEntity

data class HistoryStats(
    val countOrHour:Int,
    val point: Int,
    val completed:Int,
    val completedList: List<WorkHistoryEntity>,
    val currentList: List<WorkHistoryEntity>,
    val mostRegion: String,
    val mostCategory: String,
)