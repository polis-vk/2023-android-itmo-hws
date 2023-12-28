package ru.ok.itmo.example.utils

import android.widget.Button
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import ru.ok.itmo.example.R

fun Button.enable() = apply {
    isEnabled = true
    background = ContextCompat.getDrawable(context, R.drawable.button_active)
}
fun Button.disable() = apply {
    isEnabled = false
    background = ContextCompat.getDrawable(context, R.drawable.button_disabled)
}

fun TextInputLayout.activate() = apply {
    isEnabled = true
    background = ContextCompat.getDrawable(context, R.drawable.input_field_active)
}
fun TextInputLayout.disable() = apply {
    isEnabled = false
    background = ContextCompat.getDrawable(context, R.drawable.input_field_disabled)
}
