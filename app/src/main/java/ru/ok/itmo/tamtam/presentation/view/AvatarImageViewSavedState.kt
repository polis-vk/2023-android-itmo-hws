package ru.ok.itmo.tamtam.presentation.view

import android.os.Parcel
import android.os.Parcelable
import android.view.View

class AvatarImageViewSavedState : View.BaseSavedState {
    var backgroundColor: Int = 0
    var textColor: Int = 0
    var text: String = ""
    var textSize: Float = 0f

    constructor(superState: Parcelable?) : super(superState)

    private constructor(parcel: Parcel) : super(parcel) {
        backgroundColor = parcel.readInt()
        textColor = parcel.readInt()
        text = parcel.readString() ?: ""
        textSize = parcel.readFloat()
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        super.writeToParcel(out, flags)
        out.writeInt(backgroundColor)
        out.writeInt(textColor)
        out.writeString(text)
        out.writeFloat(textSize)
    }

    companion object CREATOR : Parcelable.Creator<AvatarImageViewSavedState> {
        override fun createFromParcel(parcel: Parcel): AvatarImageViewSavedState {
            return AvatarImageViewSavedState(parcel)
        }

        override fun newArray(size: Int): Array<AvatarImageViewSavedState?> {
            return arrayOfNulls(size)
        }
    }
}