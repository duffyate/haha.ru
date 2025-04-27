package com.example.kal.utils

import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object MyDateSerializer {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME

    fun serializeDateToString(date: ZonedDateTime): String {
        return date.format(formatter)
    }
    fun deSerializeDateToString(date: String): ZonedDateTime {
        return ZonedDateTime.parse(date, formatter)
    }
    fun String.getMinute(): String {
        val zonedDateTime = deSerializeDateToString(this)
        return zonedDateTime.minute.toString()
    }
    fun String.getHour(): String {
        val zonedDateTime = deSerializeDateToString(this)
        return zonedDateTime.hour.toString()
    }
    fun String.getDay(): String {
        val zonedDateTime = deSerializeDateToString(this)
        return zonedDateTime.dayOfMonth.toString()
    }
    fun String.getMonth(): String {
        val zonedDateTime = deSerializeDateToString(this)
        return zonedDateTime.monthValue.toString()
    }
    fun String.getYear(): String {
        val zonedDateTime = deSerializeDateToString(this)
        return zonedDateTime.year.toString()
    }
    fun String.getFullDate(): String {
        return "${getDay()}.${getMonth()}.${getYear()} в ${getMinute()}:${getHour()}"
    }
    fun String.getRelativeTimeString(): String {
        val parsedDate = deSerializeDateToString(this)
        val now = ZonedDateTime.now(parsedDate.zone)
        val duration = Duration.between(parsedDate, now)

        return when {
            duration.toSeconds() < 60 -> "${duration.toSeconds()} секунд назад"
            duration.toMinutes() < 60 -> "${duration.toMinutes()} минут назад"
            duration.toHours() < 12 -> "${duration.toHours()} часов назад"
            duration.toHours() < 24 -> "Сегодня в ${parsedDate.format(DateTimeFormatter.ofPattern("HH:mm"))}"
            duration.toDays() < 2 -> "Вчера в ${parsedDate.format(DateTimeFormatter.ofPattern("HH:mm"))}"
            duration.toDays() < 365 -> "${parsedDate.dayOfMonth}.${parsedDate.monthValue} в ${parsedDate.format(DateTimeFormatter.ofPattern("HH:mm"))}"
            else -> getFullDate()
        }
    }
}