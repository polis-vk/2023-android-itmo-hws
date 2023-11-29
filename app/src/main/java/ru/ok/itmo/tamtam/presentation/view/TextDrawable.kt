package ru.ok.itmo.tamtam.presentation.view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

class TextDrawable : Drawable() {
    companion object {
        private const val TEXT_SIZE = 20f
        private const val TEXT_COLOR = Color.WHITE
        private const val BACKGROUND_COLOR = Color.BLACK
    }

    var text: String = ""
    var backgroundColor = BACKGROUND_COLOR
    var textColor = TEXT_COLOR
    var textSize: Float
        get() = paint.textSize
        set(value) {
            paint.textSize = value
        }

    var paint = Paint().apply {
        style = Paint.Style.FILL
        textSize = TEXT_SIZE
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        isFakeBoldText = true
    }

    override fun draw(canvas: Canvas) {
        paint.color = backgroundColor
        canvas.drawRect(bounds, paint)
        if (text.isBlank()) return

        val baseLineDistance = (paint.ascent() + paint.descent()) / 2
        val x = bounds.width() / 2f
        val y = bounds.height() / 2f - baseLineDistance

        paint.color = textColor
        canvas.drawText(text, x, y, paint)
    }

    override fun getOpacity(): Int = PixelFormat.TRANSPARENT

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        paint.colorFilter = colorFilter
    }
}