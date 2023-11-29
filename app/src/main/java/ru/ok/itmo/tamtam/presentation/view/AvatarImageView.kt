package ru.ok.itmo.tamtam.presentation.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.os.Parcelable
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.utils.convertDpToPx
import kotlin.random.Random

open class AvatarImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    companion object {
        private const val DEFAULT_BACKGROUND_COLOR = Color.BLACK
        private const val DEFAULT_TEXT_COLOR = Color.WHITE
        private const val DEFAULT_TEXT_SIZE = 24f
    }

    private var textDrawable: TextDrawable = TextDrawable().apply {
        this.textSize = context.convertDpToPx(DEFAULT_TEXT_SIZE)
        this.textColor = DEFAULT_TEXT_COLOR
        this.backgroundColor = DEFAULT_BACKGROUND_COLOR
    }


    init {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView)

            val backgroundColor = typedArray.getColor(
                R.styleable.CircleImageView_civ_backgroundColor,
                DEFAULT_BACKGROUND_COLOR
            )
            textDrawable.backgroundColor = backgroundColor

            val textColor = typedArray.getColor(
                R.styleable.CircleImageView_civ_textColor,
                DEFAULT_TEXT_COLOR
            )
            textDrawable.textColor = textColor

            val textSize = typedArray.getDimension(
                R.styleable.CircleImageView_civ_textSize,
                context.convertDpToPx(DEFAULT_TEXT_SIZE)
            )
            textDrawable.textSize = textSize

            typedArray.recycle()
        }
        setImageDrawable(textDrawable)
    }

    private val clipPath = Path()

    override fun onDraw(canvas: Canvas) {
        val x = width / 2f
        val y = height / 2f
        val radius = Math.min(x, y)
        clipPath.rewind()
        clipPath.addCircle(x, y, radius, Path.Direction.CW)
        canvas.clipPath(clipPath)
        super.onDraw(canvas)
    }

    fun setAvatarBackgroundColor(@ColorRes colorId: Int) {
        textDrawable.backgroundColor = resources.getColor(colorId)
        invalidate()
    }

    fun setAvatarTextColor(@ColorRes colorId: Int) {
        textDrawable.textColor = resources.getColor(colorId)
        invalidate()
    }

    fun setText(text: String) {
        textDrawable.backgroundColor = Color.parseColor(getColorForName(text))
        val initialsForAvatar = getInitialsForAvatar(text)
        if (textDrawable.text == initialsForAvatar) return
        textDrawable.text = initialsForAvatar
        invalidate()
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val savedState = AvatarImageViewSavedState(superState)
        savedState.backgroundColor = textDrawable.backgroundColor
        savedState.textColor = textDrawable.textColor
        savedState.text = textDrawable.text
        savedState.textSize = textDrawable.textSize
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is AvatarImageViewSavedState) {
            super.onRestoreInstanceState(state.superState)
            textDrawable.backgroundColor = state.backgroundColor
            textDrawable.textColor = state.textColor
            textDrawable.text = state.text
            textDrawable.textSize = state.textSize
            invalidate()
        } else {
            super.onRestoreInstanceState(state)
        }
    }
}

private fun getInitialsForAvatar(text: String): String {
    val words = text.split("\\s+".toRegex())
    val initials = words.take(2).map { it.take(1) }
    return initials.joinToString(separator = "")
}

private fun getColorForName(name: String): String {
    val seed = name.hashCode().toLong()
    val random = Random(seed)
    val index = random.nextInt(colorsHex.size)
    return colorsHex[index]
}

private val colorsHex = listOf(
    "#bc6c25",
    "#283618",
    "#606c38",
    "#2a9d8f",
    "#f4a261",
    "#a2d2ff",
    "#003566",
    "#ffd60a",
    "#780000",
    "#8338ec",
    "#06d6a0",
    "#ff70a6",
)