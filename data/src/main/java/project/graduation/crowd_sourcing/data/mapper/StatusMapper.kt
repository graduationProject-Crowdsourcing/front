package project.graduation.crowd_sourcing.data.mapper

import project.graduation.crowd_sourcing.domain.model.WorkStatus

fun stringToWorkStatus(status: String): WorkStatus {
    return WorkStatus.values().find { it.name.equals(status, ignoreCase = true) }
        ?: WorkStatus.WORKING 
}
