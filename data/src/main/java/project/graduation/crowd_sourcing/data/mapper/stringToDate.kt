package project.graduation.crowd_sourcing.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
fun stringToDate(isoString: String): LocalDateTime? {
    return try {
        val instant = Instant.parse(isoString)
        LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    } catch (e: Exception) {
        null
    }
}
