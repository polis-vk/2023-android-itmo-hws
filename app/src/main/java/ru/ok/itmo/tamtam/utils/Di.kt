package ru.ok.itmo.tamtam.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.MapKey
import ru.ok.itmo.tamtam.App
import ru.ok.itmo.tamtam.ioc.AppComponent
import java.lang.annotation.Inherited
import javax.inject.Qualifier
import kotlin.reflect.KClass

val Context.appComponent: AppComponent
    get() = when (this) {
        is App -> {
            appComponent
        }

        else -> {
            this.applicationContext.appComponent
        }
    }

@Inherited
@Qualifier
annotation class ApplicationContext

@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)

@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val value: KClass<out ViewModel>)