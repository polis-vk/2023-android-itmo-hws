package ru.ok.itmo.example.content

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InnerViewModel : ViewModel() {
    private val _color = MutableLiveData<Int>()
    val color: LiveData<Int> get() = _color

    private val _number = MutableLiveData<Int>()
    val number: LiveData<Int> get() = _number

    init {
        _color.value = colors.random()
        _number.value = (0..1000).random()
    }

    companion object {
        private val colors = listOf(
            Color.RED,
            Color.GREEN,
            Color.BLUE,
            Color.YELLOW,
            Color.CYAN,
            Color.MAGENTA,
            Color.GRAY,
            Color.BLACK,
            Color.WHITE
        )
    }
}