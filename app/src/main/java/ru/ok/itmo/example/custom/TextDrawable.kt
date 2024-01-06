package ru.ok.itmo.example.custom

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

class TextDrawable : Drawable() {

    companion object {
        private const val DEFAULT_TEXT_SIZE = 50f
        private const val DEFAULT_TEXT_COLOR = Color.WHITE
    }

    var text: String = ""
    var textColor = DEFAULT_TEXT_COLOR

    var paint = Paint().apply {
        style = Paint.Style.FILL
        textSize = DEFAULT_TEXT_SIZE
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        isFakeBoldText = true
    }

    override fun draw(canvas: Canvas) {
        if (text.isBlank()) return

        val baseLineDistance = (paint.ascent() + paint.descent()) / 2
        paint.color = textColor
        canvas.drawText(
            text,
            bounds.width().toFloat() / 2,
            bounds.height().toFloat() / 2 - baseLineDistance,
            paint
        )
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        paint.colorFilter = cf
    }

    fun setTextSize(size: Float) {
        paint.textSize = size
    }

    override fun getOpacity(): Int = PixelFormat.TRANSPARENT
}