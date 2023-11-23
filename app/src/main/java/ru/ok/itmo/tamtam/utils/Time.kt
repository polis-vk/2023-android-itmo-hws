package ru.ok.itmo.tamtam.utils

import java.text.SimpleDateFormat

import java.util.Calendar
import java.util.Date
import java.util.Locale

fun convertTimestampForChat(timestamp: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp

    val now = Calendar.getInstance()

    val dateFormat = when {
        now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) -> {
            when {
                now.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR) -> "HH:mm"
                now.get(Calendar.WEEK_OF_YEAR) == calendar.get(Calendar.WEEK_OF_YEAR) -> "EE"
                else -> "MMM"
            }
        }

        else -> "yyyy"
    }

    val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
    return simpleDateFormat.format(Date(timestamp))
}

fun convertTimestampForMessage(timestamp: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timestamp

    val now = Calendar.getInstance()

    val dateFormat = when {
        now.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) -> {
            when {
                now.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR) -> "HH:mm"
                now.get(Calendar.WEEK_OF_YEAR) == calendar.get(Calendar.WEEK_OF_YEAR) -> "HH:mm, EEEE"
                else -> "d MMMM"
            }
        }

        else -> "yyyy"
    }

    val simpleDateFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
    return simpleDateFormat.format(Date(timestamp))
}

