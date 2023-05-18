package com.itstor.pictale.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class TimeUtils {
    companion object {
        fun getTimestamp(): String {
            return Instant.now().epochSecond.toString()
        }

        fun getTimeAgo(timestamp: String): String {
            val now = ZonedDateTime.now()
            val past = ZonedDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME)

            val years = ChronoUnit.YEARS.between(past, now)
            val months = ChronoUnit.MONTHS.between(past, now)
            val weeks = ChronoUnit.WEEKS.between(past, now)
            val days = ChronoUnit.DAYS.between(past, now)
            val hours = ChronoUnit.HOURS.between(past, now)
            val minutes = ChronoUnit.MINUTES.between(past, now)

            return when {
                years > 0 -> getTimeAgoWithFormattedDate(past.toLocalDateTime(), "yyyy/MM/dd")
                months > 0 -> "$months ${if (months == 1L) "month" else "months"} ago"
                weeks > 0 -> "$weeks ${if (weeks == 1L) "week" else "weeks"} ago"
                days > 1 -> "$days ${if (days == 1L) "day" else "days"} ago"
                hours > 1 -> "$hours ${if (hours == 1L) "hour" else "hours"} ago"
                minutes > 1 -> "$minutes ${if (minutes == 1L) "minute" else "minutes"} ago"
                else -> "just now"
            }
        }

        private fun getTimeAgoWithFormattedDate(past: LocalDateTime, pattern: String): String {
            val formatter = DateTimeFormatter.ofPattern(pattern)
            return "on ${past.format(formatter)}"
        }
    }
}
