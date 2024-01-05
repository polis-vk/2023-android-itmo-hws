package ru.ok.itmo.example.utils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import ru.ok.itmo.example.R
import kotlin.math.min
import kotlin.random.Random

class CustomAvatarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var fillPaint: Paint = Paint()
    private var textPaint: Paint = Paint()
    private var backgroundColor: Int
    private var bitmap: Bitmap? = null
    private var textSizeW: Int = 0
    private var textSizeH: Int = 0
    private var text: String = ""
    private val circle = RectF()
    private val avatarViewUtils: AvatarViewUtils = AvatarViewUtils()

    init {
        backgroundColor =
            Color.rgb(
                Random.nextInt(0, 215),
                Random.nextInt(0, 215),
                Random.nextInt(0, 215)
            )
        fillPaint.color = backgroundColor

        textPaint.color = Color.WHITE
        textPaint.textSize = resources.getDimension(R.dimen._40dp)
    }


    fun setBgColor(@ColorRes colorId: Int) {
        backgroundColor = resources.getColor(colorId, context.theme)
        invalidate()
    }


    fun setText(text: String) {
        if (text.isNotBlank()) {
            val title = avatarViewUtils.getTitle(text)
            val bounds = Rect()
            textPaint.getTextBounds(title, 0, title.length, bounds)

            this.text = title
            textSizeW = bounds.width()
            textSizeH = bounds.height()
        }

        invalidate()
    }

    fun setImage(bitmap: Bitmap) {
        this.bitmap = bitmap
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val save = canvas.save()
        val radius = min(width, height) / 2f

        circle.set(0f, 0f, radius * 2f, radius * 2f)


        if (bitmap != null) {
            bitmap!!.width = (radius * 2).toInt()
            bitmap!!.height = (radius * 2).toInt()
            canvas.drawBitmap(bitmap!!, 0f, 0f, fillPaint)

            return
        }


        val char: Char
        if (text.isNotEmpty()) {
            char = text.get(0)
        } else {
            char = 'A'
        }

        val num = char.toInt()

        backgroundColor =
            Color.rgb(
                num + 30,
                num - 30,
                num
            )
        fillPaint.apply { color = backgroundColor }
        canvas.drawRoundRect(circle, radius, radius, fillPaint)
        canvas.drawText(text, radius - textSizeW / 2, radius + textSizeH / 2, textPaint)
        canvas.restoreToCount(save)
    }
}
