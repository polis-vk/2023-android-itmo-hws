package ru.ok.itmo.tamtam.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<E : BaseState>(initial: E) : ViewModel() {
    private val _state = MutableStateFlow(initial)
    val state: StateFlow<E>
        get() = _state

    fun setState(newState: E) = _state.tryEmit(newState)
}