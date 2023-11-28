package ru.ok.itmo.example

import android.app.Application
import ru.ok.itmo.example.repositories.Repository
import ru.ok.itmo.example.room.AppDatabase

class DaApplication: Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { Repository(database.getDao()) }
}