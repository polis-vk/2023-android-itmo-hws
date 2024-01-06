package ru.ok.itmo.example.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import ru.ok.itmo.example.R
import kotlin.random.Random


open class AvatarImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    companion object {
        private const val DEFAULT_BG_COLOR = Color.RED
    }

    private var text: String? = null
    protected var textDrawable = TextDrawable()

    private var bitmap: Bitmap? = null

    private val clipPath = Path()

    private val paint = Paint().apply {
        foreground = textDrawable
        color = DEFAULT_BG_COLOR
        isAntiAlias = true
    }

    init {
        if (attrs != null) {
            val styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)

            paint.color = styledAttrs.getColor(
                R.styleable.CircleImageView_cv_backgroundColor,
                Color.rgb(
                    Random.nextInt(0, 255),
                    Random.nextInt(0, 255),
                    Random.nextInt(0, 255)
                )
            )
            textDrawable.textColor =
                styledAttrs.getColor(
                    R.styleable.CircleImageView_cv_textColor,
                    textDrawable.textColor
                )

            styledAttrs.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        val x = width / 2f
        val y = height / 2f
        val radius = Math.min(x, y)

        clipPath.addCircle(x, y, radius, Path.Direction.CW)
        canvas.clipPath(clipPath)

        super.onDraw(canvas)

        if (bitmap != null) {
            bitmap!!.width = (radius * 2).toInt()
            bitmap!!.height = (radius * 2).toInt()
            canvas.drawBitmap(bitmap!!, 0f, 0f, paint)

            return
        }

        canvas.drawCircle(x, y, radius, paint)
    }

    fun setBgColor(@ColorRes colorId: Int) {
        paint.color = resources.getColor(colorId, context.theme)
        invalidate()
    }

    fun setTextColor(@ColorRes colorId: Int) {
        textDrawable.textColor = resources.getColor(colorId, context.theme)
        invalidate()
    }

    fun setText(text: String?) {
        if (this.text == text) return

        this.text = text

        if (!text.isNullOrBlank()) {
            this.text = text.split(" ").take(2).joinToString("") { it[0].uppercase() }
            textDrawable.text = this.text!!
        } else {
            textDrawable.text = ""
        }

        invalidate()
    }

    fun setImage(bitmap: Bitmap) {
        this.bitmap = bitmap
        invalidate()
    }


}