package project.graduation.crowd_sourcing.data.mapper.statistics

import android.os.Build
import androidx.annotation.RequiresApi
import project.graduation.crowd_sourcing.data.response.statistics.DetailResponse
import project.graduation.crowd_sourcing.data.response.statistics.ItemListPriceResponse
import project.graduation.crowd_sourcing.data.response.statistics.ItemMinMaxPriceResponse
import project.graduation.crowd_sourcing.data.response.statistics.MartListPriceResponse
import project.graduation.crowd_sourcing.data.response.statistics.MartMinMaxPriceResponse
import project.graduation.crowd_sourcing.domain.model.entity.statistics.DetailEntity
import project.graduation.crowd_sourcing.domain.model.entity.statistics.ItemListPriceEntity
import project.graduation.crowd_sourcing.domain.model.entity.statistics.ItemMinMaxPriceEntity
import project.graduation.crowd_sourcing.domain.model.entity.statistics.MartListPriceEntity
import project.graduation.crowd_sourcing.domain.model.entity.statistics.MartMinMaxPriceEntity

@RequiresApi(Build.VERSION_CODES.O)
fun DetailResponse.toEntity() = DetailEntity(
    commission = this.commission,
    commissionregion = commissionregion,
    category = this.category,
    commissionDate = this.commissionDate,
    expirationDate = this.expirationDate,
    commisionCount = this.commisionCount,
    commisionpoint = this.commisionpoint
)

fun MartListPriceResponse.toEntity() = MartListPriceEntity(
    region = this.region,
    category = this.category,
    mart = this.mart,
    categoryPrice =  categoryPrice?.toInt() ?: 0
)

fun ItemMinMaxPriceResponse.toEntity() = ItemMinMaxPriceEntity(
    region = this.region,
    category = this.category,
    item = this.item,
    itemPrice = this.itemPrice,
    mart = this.mart,
    averagePrice = this.averagePrice,
    difference = this.difference
)

fun ItemListPriceResponse.toEntity() = ItemListPriceEntity(
    region = this.region,
    category = this.category,
    item = this.item,
    averagePrice = this.averagePrice
)

fun MartMinMaxPriceResponse.toEntity() = MartMinMaxPriceEntity(
    region = this.region,
    category = this.category,
    mart = this.mart,
    categoryPrice = this.categoryPrice,
    difference = this.difference
)