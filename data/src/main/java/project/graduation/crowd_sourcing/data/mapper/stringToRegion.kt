package project.graduation.crowd_sourcing.data.mapper

import project.graduation.crowd_sourcing.domain.model.Region

fun stringToRegion(region:String):Region = Region.from(region)
