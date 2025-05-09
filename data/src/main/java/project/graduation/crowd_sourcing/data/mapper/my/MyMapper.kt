package project.graduation.crowd_sourcing.data.mapper.my

import android.os.Build
import androidx.annotation.RequiresApi
import project.graduation.crowd_sourcing.data.mapper.parseIso8601ToDateSafe
import project.graduation.crowd_sourcing.data.response.my.RecentCommissionResponse
import project.graduation.crowd_sourcing.data.response.my.RecentWorkResponse
import project.graduation.crowd_sourcing.domain.model.Region
import project.graduation.crowd_sourcing.domain.model.entity.my.RecentCommissionEntity
import project.graduation.crowd_sourcing.domain.model.entity.my.RecentWorkEntity
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
fun RecentCommissionResponse.toEntity(): RecentCommissionEntity = RecentCommissionEntity(
    id = this.id,
    commission = this.commission,
    region = Region.from(this.region),
    category = this.category,
    commissionDate = parseIso8601ToDateSafe(this.commissionDate) ?: Date(),
    commissionPoint = this.commissionPoint
)

@RequiresApi(Build.VERSION_CODES.O)
fun RecentWorkResponse.toEntity(): RecentWorkEntity = RecentWorkEntity(
    id = this.id,
    work = this.work,
    region = Region.from(this.region),
    category = this.category,
    item = this.item,
    itemPrice = this.itemPrice,
    workDate = parseIso8601ToDateSafe(this.workDate) ?: Date()
)