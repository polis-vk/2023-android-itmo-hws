package ru.ok.itmo.example.CustomView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import ru.ok.itmo.example.R
import java.util.Random


@SuppressLint("Recycle")
class AvatarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {
    private val paint = Paint()
    private var image_link: String? = null
    private var textColor: Int? = null
    private var name: String? = null
    var has_image: Boolean = true

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AvatarView)
        textColor = typedArray.getColor(R.styleable.AvatarView_textColor, generateRandomColor())

        paint.color = typedArray.getColor(R.styleable.AvatarView_backgroundColor, generateRandomColor())
        paint.textSize = typedArray.getDimension(R.styleable.AvatarView_textSize, 30F)
        paint.textAlign = Paint.Align.CENTER

        paint.style = Paint.Style.FILL

        typedArray.recycle()
    }

    fun setInitials(name: String) {
        val words = name.split(" ")
        val initials = StringBuilder()

        for (word in words) {
            if (word.isNotEmpty()) {
                val firstLetter = word[0].uppercaseChar()
                initials.append(firstLetter)
                if (word.length > 1) {
                    val secondLetter = word[1].uppercaseChar()
                    initials.append(secondLetter)
                }
                break;
            }
        }
        this.name = initials.toString()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawCircleButton(canvas)

    }

    private fun drawInitials(canvas: Canvas) {
        name?.let { name ->
            if (name.isNotEmpty()) {
                paint.color = textColor ?: Color.WHITE

                val centerX = width / 2f
                val centerY = height / 2f

                val textHeight = getTextHeight(name, paint)
                val yOffset = (textHeight / 2) - (paint.descent() + paint.ascent()) / 2

                canvas.drawText(name, centerX, centerY + yOffset, paint)
            }
        }
    }

    private fun drawCircleButton(canvas: Canvas) {
        val centerX = width / 2f
        val centerY = width / 2f
        val radius = width.coerceAtMost(height) / 3f
        canvas.drawCircle(centerX, centerY, radius, paint)
        drawInitials(canvas)
    }

    private fun getTextHeight(text: String, paint: Paint): Float {
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)
        return bounds.height().toFloat()
    }

    fun generateRandomColor(): Int {
        val random = Random()
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
    }

}
