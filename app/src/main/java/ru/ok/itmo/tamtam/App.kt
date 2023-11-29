package ru.ok.itmo.tamtam

import android.app.Application
import ru.ok.itmo.tamtam.ioc.AppComponent
import ru.ok.itmo.tamtam.ioc.DaggerAppComponent

class App : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory()
            .create(this)
    }
}