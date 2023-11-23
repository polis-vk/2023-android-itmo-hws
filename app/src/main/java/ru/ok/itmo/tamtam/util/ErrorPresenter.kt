package ru.ok.itmo.tamtam.util

import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import ru.ok.itmo.tamtam.domain.ErrorType

data object ErrorPresenter {
    fun present(error: Throwable?, view: TextView) {
        if (error == null) {
            view.text = ""
            view.isInvisible = true
            return
        }
        view.isVisible = true
        when (error) {
            is ErrorType.Unknown -> view.text = TextPresentObjects.unknown
            is ErrorType.InternetConnection -> view.text =
                TextPresentObjects.internetConnection

            is ErrorType.Unauthorized -> view.text = TextPresentObjects.unauthorized
            is ErrorType.IncorrectToken -> view.text = TextPresentObjects.authToken
            else -> view.text = TextPresentObjects.error
        }
    }
}
