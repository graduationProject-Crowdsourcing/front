package project.graduation.crowd_sourcing.data.local

import project.graduation.crowd_sourcing.domain.model.Noti

fun NotiEntity.toDomain(): Noti = Noti(
    id = id,
    title = title,
    content = content
)


fun Noti.toEntity(): NotiEntity = NotiEntity(
    id = id,
    title = title,
    content = content
)