package project.graduation.crowd_sourcing.domain.model.entity.my

import android.provider.ContactsContract.CommonDataKinds.Nickname

data class ProfileEntity (
    val nickname: String,
    val point: Int
)