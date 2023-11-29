package ru.ok.itmo.tamtam.utils

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.View

fun Context.getThemeColor(attrId: Int): Int {
    val typedValue = TypedValue()
    val theme = theme
    theme.resolveAttribute(attrId, typedValue, true)
    return typedValue.data
}

fun Activity.setStatusBarTextDark(isDark: Boolean) {
    val decor = this.window.decorView
    if (isDark) {
        decor.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    } else {
        decor.systemUiVisibility = 0
    }
}

fun Context.convertDpToPx(dp: Float): Float {
    return dp * this.resources.displayMetrics.density
}