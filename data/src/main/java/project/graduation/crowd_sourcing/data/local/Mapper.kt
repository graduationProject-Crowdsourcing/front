package project.graduation.crowd_sourcing.data.local

import project.graduation.crowd_sourcing.domain.model.Noti

fun NotiEntity.toDomain(): Noti = Noti(
    title = title,
    content = content
)


fun Noti.toEntity(): NotiEntity = NotiEntity(
    title = title,
    content = content
)