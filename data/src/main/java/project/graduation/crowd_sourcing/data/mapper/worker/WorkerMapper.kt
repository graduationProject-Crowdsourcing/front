package project.graduation.crowd_sourcing.data.mapper.worker

import android.os.Build
import androidx.annotation.RequiresApi
import project.graduation.crowd_sourcing.data.mapper.stringToRegion
import project.graduation.crowd_sourcing.data.mapper.stringToWorkStatus
import project.graduation.crowd_sourcing.data.response.worker.WorkCountEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkCountResponse
import project.graduation.crowd_sourcing.data.response.worker.WorkHistoryEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkHistoryResponse
import project.graduation.crowd_sourcing.data.response.worker.WorkHourEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkHourResponse
import project.graduation.crowd_sourcing.data.response.worker.WorkMostEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkMostResponse
import project.graduation.crowd_sourcing.data.response.worker.WorkOngoingResponse
import project.graduation.crowd_sourcing.data.response.worker.WorkPointEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkPointResponse
import project.graduation.crowd_sourcing.domain.model.Category

fun WorkCountResponse.toEntity() = WorkCountEntity(
    workCount = workCount
)

fun WorkOngoingResponse.toEntity() = WorkHistoryEntity(
    id = workAssignmentId,
    commission = commission,
    commissionCount = commissionCount,
    commissionPoint = commissionPoint,
    commissionRegion = this.commissionRegion,
    commissionDate = commissionDate,
    commissionStatus = stringToWorkStatus(commissionStatus),
    memberId = memberId,
    martName = martName,
    category = category
)

@RequiresApi(Build.VERSION_CODES.O)
fun WorkHistoryResponse.toEntity() = WorkHistoryEntity(
    id = id,
    commission = commission,
    commissionCount = commissionCount,
    commissionPoint = commissionPoint,
    commissionRegion = this.commissionRegion,
    commissionDate = commissionDate,
    commissionStatus = stringToWorkStatus(commissionStatus),
    memberId = memberId,
    category = commissionCategory
)

fun WorkHourResponse.toEntity() = WorkHourEntity(
    workhour = workhour
)

fun WorkMostResponse.toEntity() = WorkMostEntity(
    region = stringToRegion(this.region),
    item = item,
    dayOfWeek = dayOfWeek
)

fun WorkPointResponse.toEntity() = WorkPointEntity(
    totalPoints = totalPoints
)
