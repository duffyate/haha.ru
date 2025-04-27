package com.example.kal.utils

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

object TimeManager {
    fun convertToLocalDateTime(supabaseDate: String): ZonedDateTime {
        val parsedDate = OffsetDateTime.parse(supabaseDate.replace(" ", "T"))
        val userTimeZone = ZoneId.systemDefault()
        return parsedDate.atZoneSameInstant(userTimeZone)
    }
}