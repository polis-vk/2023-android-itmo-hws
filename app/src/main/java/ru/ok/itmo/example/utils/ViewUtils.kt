package ru.ok.itmo.example.utils

import android.view.View

class ViewUtils {
    companion object {
        fun visibleFirst(vararg views: View) {
            if (views.isEmpty()) return
            views[0].visibility = View.VISIBLE
            invisible(*views.slice(1 until views.size).toTypedArray())
        }

        fun invisible(vararg views: View) {
            for (view in views)
                view.visibility = View.INVISIBLE
        }
    }
}