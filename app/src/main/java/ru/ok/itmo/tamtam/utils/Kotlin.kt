package ru.ok.itmo.tamtam.utils


fun Boolean?.orTrue() = this ?: true

fun Boolean?.orFalse() = this ?: false
