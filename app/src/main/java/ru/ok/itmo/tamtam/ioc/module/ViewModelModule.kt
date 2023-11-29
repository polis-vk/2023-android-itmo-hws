package ru.ok.itmo.tamtam.ioc.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.ok.itmo.tamtam.ioc.scope.AppComponentScope
import ru.ok.itmo.tamtam.presentation.stateholder.ChatViewModel
import ru.ok.itmo.tamtam.presentation.stateholder.ChatsViewModel
import ru.ok.itmo.tamtam.presentation.stateholder.ContactsViewModel
import ru.ok.itmo.tamtam.presentation.stateholder.LoginViewModel
import ru.ok.itmo.tamtam.utils.ViewModelFactory
import ru.ok.itmo.tamtam.utils.ViewModelKey

@Module
interface ViewModelModule {
    @Binds
    @[IntoMap ViewModelKey(LoginViewModel::class)]
    fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(ChatsViewModel::class)]
    fun bindChatsViewModel(chatsViewModel: ChatsViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(ChatViewModel::class)]
    fun bindChatViewModel(chatViewModel: ChatViewModel): ViewModel

    @Binds
    @[IntoMap ViewModelKey(ContactsViewModel::class)]
    fun bindContactsViewModel(contactsViewModel: ContactsViewModel): ViewModel

    @AppComponentScope
    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}