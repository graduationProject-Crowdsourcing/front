package project.graduation.crowd_sourcing.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
fun parseIso8601ToDateSafe(isoString: String): Date? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Date.from(Instant.parse(isoString))
    } else {
        null
    }
}
