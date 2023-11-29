package ru.ok.itmo.tamtam.utils

import androidx.lifecycle.LifecycleOwner

interface OnBackPressed {
    fun addCustomOnBackPressed(lifecycleOwner: LifecycleOwner, body: () -> Unit)
}