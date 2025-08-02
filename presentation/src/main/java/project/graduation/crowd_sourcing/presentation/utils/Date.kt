package project.graduation.crowd_sourcing.presentation.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
fun getTimeAgo(date: LocalDateTime): String {
    val now = LocalDateTime.now()
    val minutes = ChronoUnit.MINUTES.between(date, now)
    val hours = ChronoUnit.HOURS.between(date, now)
    val days = ChronoUnit.DAYS.between(date, now)

    return when {
        minutes < 1 -> "방금 전"
        minutes < 60 -> "${minutes}분 전"
        hours < 24 -> "${hours}시간 전"
        days < 7 -> "${days}일 전"
        days < 30 -> "${days / 7}주 전"
        days < 365 -> "${days / 30}달 전"
        else -> "${days / 365}년 전"
    }
}


@RequiresApi(Build.VERSION_CODES.O)
val twoDaysAgo = LocalDateTime.now().minusDays(2)