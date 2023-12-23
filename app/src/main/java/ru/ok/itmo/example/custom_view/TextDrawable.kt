package ru.ok.itmo.example.custom_view

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

class TextDrawable : Drawable() {

    companion object {
        private const val DEFAULT_BORDER_WIDTH_DP = 2f
        private const val TEXT_SIZE = 130f
        private const val TEXT_COLOR = Color.WHITE
        private const val BACKGROUND_COLOR = Color.BLACK
    }

    var text: String = "kok"
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textSize = TEXT_SIZE
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        isFakeBoldText = true
    }

    var backgroundColor = BACKGROUND_COLOR
    var textColor = TEXT_COLOR

    override fun draw(canvas: Canvas) {
        if (text.isBlank()) return

        paint.color = backgroundColor
        canvas.drawRect(bounds, paint)

        val baseLineDistance = (paint.ascent() + paint.descent()) / 2
        val x = bounds.width() / DEFAULT_BORDER_WIDTH_DP
        val y = bounds.height() / DEFAULT_BORDER_WIDTH_DP - baseLineDistance

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

    fun setTextSize(size: Float) {
        paint.textSize = size
    }
}