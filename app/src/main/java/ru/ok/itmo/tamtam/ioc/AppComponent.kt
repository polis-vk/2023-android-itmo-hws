package ru.ok.itmo.tamtam.ioc

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.ok.itmo.tamtam.App
import ru.ok.itmo.tamtam.MainActivity
import ru.ok.itmo.tamtam.data.scarlet.api.MessageApi
import ru.ok.itmo.tamtam.ioc.module.RetrofitModule
import ru.ok.itmo.tamtam.ioc.module.RoomModule
import ru.ok.itmo.tamtam.ioc.module.ScarletModule
import ru.ok.itmo.tamtam.ioc.module.ViewModelModule
import ru.ok.itmo.tamtam.ioc.scope.AppComponentScope
import ru.ok.itmo.tamtam.presentation.fragment.ChatFragment
import ru.ok.itmo.tamtam.presentation.fragment.ChatsFragment
import ru.ok.itmo.tamtam.presentation.fragment.ContactsFragment
import ru.ok.itmo.tamtam.presentation.fragment.LoginFragment
import ru.ok.itmo.tamtam.utils.ApplicationContext


@AppComponentScope
@Component(modules = [ViewModelModule::class, RetrofitModule::class, RoomModule::class, ScarletModule::class])
interface AppComponent {
    fun inject(application: App)
    fun inject(mainActivity: MainActivity)
    fun inject(chatsFragment: ChatsFragment)
    fun inject(chatFragment: ChatFragment)
    fun inject(loginFragment: LoginFragment)
    fun inject(loginFragment: ContactsFragment)

    fun getMessageApi(): MessageApi

    @Component.Factory
    interface Factory {
        fun create(
            @[BindsInstance ApplicationContext] context: Context
        ): AppComponent
    }
}