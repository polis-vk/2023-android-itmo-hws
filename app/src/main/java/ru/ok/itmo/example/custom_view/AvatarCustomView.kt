package ru.ok.itmo.example.custom_view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.annotation.ColorRes
import ru.ok.itmo.example.R

class AvatarCustomView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : androidx.appcompat.widget.AppCompatImageView(context, attr, defStyleAttr) {

    companion object {
        private const val DEFAULT_BACKGROUND_COLOR = Color.GRAY
        private const val DEFAULT_TEXT_COLOR = Color.WHITE
    }

    private var backgroundColor = DEFAULT_BACKGROUND_COLOR

    private val avatarPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private var text: String? = null
    private var textDrawable = TextDrawable()

    private val clipPath = Path()

    init {
        setText("")
        if (attr != null) {
            val a = context.obtainStyledAttributes(attr, R.styleable.AvatarCustomView)

            textDrawable.backgroundColor = a.getColor(
                R.styleable.AvatarCustomView_cv_backgroundColor,
                DEFAULT_BACKGROUND_COLOR
            )

            textDrawable.textColor = a.getColor(
                R.styleable.AvatarCustomView_cv_textColor,
                DEFAULT_TEXT_COLOR
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        val x = width / 2f
        val y = height / 2f
        val radius = x.coerceAtMost(y)

        clipPath.addCircle(x, y, radius, Path.Direction.CW)
        canvas.clipPath(clipPath)

        super.onDraw(canvas)
    }

    fun setTextColor(@ColorRes colorId: Int) {
        textDrawable.textColor = resources.getColor(colorId, context.theme)
        invalidate()
    }

    fun setCvBackgroundColor(@ColorRes colorId: Int) {
        textDrawable.backgroundColor = resources.getColor(colorId, context.theme)
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setupPaint()
    }

    private fun setupPaint() {
        avatarPaint.color = backgroundColor
    }

    fun setText(text: String?) {
        this.text = text

        if (!text.isNullOrBlank()) {
            textDrawable.text = text
            setImageDrawable(textDrawable)
        }
        invalidate()
    }

    fun setImage(bitmap: Bitmap) {
        setImageDrawable(BitmapDrawable(context.resources, bitmap))
        invalidate()
    }
}