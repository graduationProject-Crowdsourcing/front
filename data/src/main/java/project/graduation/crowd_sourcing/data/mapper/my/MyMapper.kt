package project.graduation.crowd_sourcing.data.mapper.my

import android.os.Build
import androidx.annotation.RequiresApi
import project.graduation.crowd_sourcing.data.mapper.stringToRegion
import project.graduation.crowd_sourcing.data.response.my.RecentCommissionResponse
import project.graduation.crowd_sourcing.data.response.my.RecentWorkResponse
import project.graduation.crowd_sourcing.domain.model.entity.my.RecentCommissionEntity
import project.graduation.crowd_sourcing.domain.model.entity.my.RecentWorkEntity
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
fun RecentCommissionResponse.toEntity(): RecentCommissionEntity = RecentCommissionEntity(
    id = this.workId,
    commission = this.workName,
    region = stringToRegion(this.region),
    category = this.category,
    commissionDate = this.createdAt,
    commissionPoint = this.workpoint
)

@RequiresApi(Build.VERSION_CODES.O)
fun RecentWorkResponse.toEntity(): RecentWorkEntity = RecentWorkEntity(
    id = this.workId,
    work = this.workName,
    region = stringToRegion(""),
    category = "",
    item = "",
    itemPrice = 0,
    workDate = LocalDateTime.now()
)