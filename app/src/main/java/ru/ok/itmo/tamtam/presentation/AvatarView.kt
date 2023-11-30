package ru.ok.itmo.tamtam.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import ru.ok.itmo.tamtam.R
import kotlin.random.Random

class AvatarView(context: Context, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatImageView(context, attrs) {

    private var image: String? = null
    private var initials: String? = null
    private var textColor: Int = Color.WHITE
    private var backgroundColor: Int = ContextCompat.getColor(context, R.color.avatar_background)
    private var textSize: Float = resources.getDimension(R.dimen.avatar_text_size)
    private var didLoad = true

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)

    init {
        fetchAttributes(attrs)
        setupBackground()
        setupPaint()
        setupTextPaint()
    }

    private fun tryToLoadImage() {
        Glide.with(context)
            .load(image)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    didLoad = false
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    setBackground(Color.TRANSPARENT)
                    didLoad = true
                    return false
                }

            })
            .circleCrop()
            .into(this)
    }

    private fun setupBackground() {
        val random = Random(System.currentTimeMillis())
        backgroundColor = Color.argb(
            255,
            random.nextInt(256),
            random.nextInt(256),
            random.nextInt(256)
        )
    }

    private fun setupTextPaint() {
        textPaint.apply {
            color = textColor
            textAlign = Paint.Align.CENTER
            textSize = this@AvatarView.textSize
        }
    }

    private fun setupPaint() {
        paint.apply {
            color = backgroundColor
            style = Paint.Style.FILL
        }
    }

    private fun fetchAttributes(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AvatarView)
        try {
            textColor = typedArray.getColor(R.styleable.AvatarView_customTextColor, textColor)
            textSize = typedArray.getDimension(R.styleable.AvatarView_customTextSize, textSize)
            backgroundColor =
                typedArray.getColor(R.styleable.AvatarView_customBackground, backgroundColor)
        } finally {
            typedArray.recycle()
        }
    }

    fun setInitials(initials: String) {
        this.initials = initials
        invalidate()
    }

    fun setImage(imageLink: String?) {
        this.image = imageLink
        tryToLoadImage()
        invalidate()
    }

    fun setTextColor(color: Int) {
        textColor = color
        textPaint.color = textColor
        invalidate()
    }

    fun setTextSize(size: Float) {
        textSize = size
        invalidate()
    }

    fun setBackground(color: Int) {
        backgroundColor = color
        paint.color = backgroundColor
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()

        canvas.drawCircle(width / 2f, height / 2f, width / 2f, paint)
        if (!didLoad) {
            drawInitials(canvas)
        }

        postInvalidate()
        canvas.restore()
    }

    private fun drawInitials(canvas: Canvas) {
        if (!initials.isNullOrBlank()) {
            canvas.drawText(
                initials!!,
                width / 2f,
                height / 2f - (textPaint.descent() + textPaint.ascent()) / 2f,
                textPaint
            )
        }
    }


}