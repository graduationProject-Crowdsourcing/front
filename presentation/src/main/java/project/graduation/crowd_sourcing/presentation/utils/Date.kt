package project.graduation.crowd_sourcing.presentation.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun getTimeAgo(date: Date): String {
    val now = System.currentTimeMillis()
    val time = date.time
    val diff = now - time

    return when {
        diff < TimeUnit.MINUTES.toMillis(1) -> "방금 전"
        diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)}분 전"
        diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)}시간 전"
        diff < TimeUnit.DAYS.toMillis(7) -> "${TimeUnit.MILLISECONDS.toDays(diff)}일 전"
        else -> SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).format(date)
    }
}

val calendar = Calendar.getInstance().apply {
    add(Calendar.DAY_OF_YEAR, -2) // 2일 전 날짜
}
val twoDaysAgo = calendar.time