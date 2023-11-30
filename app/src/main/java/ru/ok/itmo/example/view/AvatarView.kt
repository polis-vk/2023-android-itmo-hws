package ru.ok.itmo.example.view

import ru.ok.itmo.example.R
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import kotlin.math.min
import kotlin.random.Random

class AvatarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private var backgroundColor: Int
    private var textColor: Int
    private var text: Triple<String, Int, Int> = Triple("", 0, 0)
    private var bitmap: Bitmap? = null

    private val circle = RectF()

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { textSize = 100f }

    init {
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.AvatarView, defStyleAttr, defStyleRes)

        try {
            backgroundColor = a.getColor(
                R.styleable.AvatarView_backgroundColor,
                Color.rgb(
                    Random.nextInt(0, 255),
                    Random.nextInt(0, 255),
                    Random.nextInt(0, 255)
                )
            )
            textColor = a.getColor(R.styleable.AvatarView_textColor,
                resources.getColor(R.color.white, context.theme))
        } finally {
            a.recycle()
        }
    }

    fun setBgColor(@ColorRes colorId: Int) {
        backgroundColor = resources.getColor(colorId, context.theme)
        invalidate()
    }

    fun setTextColor(@ColorRes colorId: Int) {
        textColor = resources.getColor(colorId, context.theme)
        invalidate()
    }

    fun setText(text: String) {
        if (text.isNotBlank()) {
            val title = text.split("\\s+".toRegex())
                .take(2)
                .joinToString("") { it[0].uppercase() }

            val c = 255 / 2
            val h = title.sumOf { it.code }

            fun shift(rng: Int) = c - rng / 2 + h % rng

            backgroundColor = Color.rgb(shift(180), shift(120), shift(50))

            val bounds = Rect()
            paint.getTextBounds(title, 0, title.length, bounds)

            this.text = Triple(title, bounds.width(), bounds.height())
        }

        invalidate()
    }

    fun setImage(bitmap: Bitmap) {
        this.bitmap = bitmap
        invalidate()
    }

    private fun onDrawDot(canvas: Canvas) {
        val save = canvas.save()

        val radius = min(width, height) / 2f

        paint.apply { color = this@AvatarView.backgroundColor }

        circle.set(0f, 0f, radius * 2f, radius * 2f)

        if (bitmap != null) {
            bitmap!!.width = (radius * 2).toInt()
            bitmap!!.height = (radius * 2).toInt()
            canvas.drawBitmap(bitmap!!, 0f, 0f, paint)

            return
        }

        canvas.drawRoundRect(circle, radius, radius, paint)

        paint.apply { color = this@AvatarView.textColor }

        val (title, w, h) = text

        canvas.drawText(title, radius - w / 2, radius + h / 2, paint)

        canvas.restoreToCount(save)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        onDrawDot(canvas)
    }
}
