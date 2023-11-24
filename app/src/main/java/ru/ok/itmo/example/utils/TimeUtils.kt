package ru.ok.itmo.example.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TimeUtils {
    companion object {
        fun getDayTime(seconds: Long): String {
            return SimpleDateFormat("HH:mm", Locale("ru"))
                .format(Date(1000L * seconds))
                .format()
        }
    }
}