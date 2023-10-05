package ru.ok.itmo.example

import androidx.lifecycle.ViewModel
import kotlin.random.Random

class BaseViewModel : ViewModel() {
    val fragmentsCount = Random.nextInt(3, 6)
}