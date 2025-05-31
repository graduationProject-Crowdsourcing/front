package project.graduation.crowd_sourcing.data.mapper.userpoint

import android.os.Build
import androidx.annotation.RequiresApi
import project.graduation.crowd_sourcing.data.mapper.stringToDate
import project.graduation.crowd_sourcing.data.mapper.stringToRegion
import project.graduation.crowd_sourcing.data.response.userpoint.UserPointHistoryResponse
import project.graduation.crowd_sourcing.domain.model.entity.userpoint.UserPointHistoryEntity
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
fun UserPointHistoryResponse.toEntity(): UserPointHistoryEntity = UserPointHistoryEntity(
    type = this.type,
    region = stringToRegion(this.region),
    item = this.item,
    point = this.point,
    date = this.date
)