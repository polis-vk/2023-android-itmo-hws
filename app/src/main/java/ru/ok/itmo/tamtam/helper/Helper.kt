package ru.ok.itmo.tamtam.helper

import android.content.Context
import android.view.View
import android.view.Window
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import ru.ok.itmo.tamtam.R

class Helper {
    companion object {
        const val DEBUG_TAG = "ru.ok.itmo.hw_debug"

        fun setButtonColor(button: Button, context: Context, color: Int) {
            val tintedBackground = DrawableCompat.wrap(button.background).mutate()
            DrawableCompat.setTint(tintedBackground, getColor(context, color))
            button.background = tintedBackground
        }

        fun getColor(context: Context, color: Int): Int {
            return ContextCompat.getColor(
                context, color
            )
        }

        fun setAccentStatusBar(context: Context, window: Window) {
            window.statusBarColor = getColor(context, R.color.accent)
            window.decorView.systemUiVisibility = 0
        }

        fun setLightStatusBar(context: Context, window: Window) {
            window.statusBarColor = getColor(context, R.color.white)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}