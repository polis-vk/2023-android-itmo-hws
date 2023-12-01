package ru.ok.itmo.tuttut.messenger.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import ru.ok.itmo.tuttut.R


class AvatarView : View {

    companion object {
        private fun defaultPaint() = Paint(Paint.ANTI_ALIAS_FLAG)
    }

    private var initials: String = ""
    private var textColor: Int = Color.WHITE
    private var backgroundColor: Int = Color.BLUE
    private var textPaint = defaultPaint()
    private var backgroundPaint = defaultPaint()
    private val bounds = Rect()
    private val rect = Rect()
    private var bitmap: Bitmap? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AvatarView)

        textColor = typedArray.getColor(R.styleable.AvatarView_textColor, Color.WHITE)
        backgroundColor = typedArray.getColor(R.styleable.AvatarView_backgroundColor, Color.LTGRAY)

        textPaint.color = textColor
        backgroundPaint.color = backgroundColor

        typedArray.recycle()
    }

    fun setImage(bitmap: Bitmap) {
        this.bitmap = bitmap
        initials = ""
        invalidate()
    }

    fun setInitials(initials: String) {
        this.initials = initials
        bitmap = null
        invalidate()
    }

    fun setTextColor(color: Int) {
        textColor = color
        textPaint.color = textColor
        invalidate()
    }

    fun clear() {
        initials = ""
        bitmap = null
    }

    override fun setBackgroundColor(color: Int) {
        backgroundColor = color
        backgroundPaint.color = backgroundColor
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawCircle(width / 2f, height / 2f, width / 2f, backgroundPaint)

        getDrawingRect(rect)
        bitmap?.let { canvas.drawBitmap(it, null, rect, null) }

        textPaint.textSize = height / 2f
        textPaint.getTextBounds(initials, 0, initials.length, bounds)
        val x = width / 2f - bounds.centerX()
        val y = height / 2f - bounds.centerY()
        canvas.drawText(initials, x, y, textPaint)
    }
}
