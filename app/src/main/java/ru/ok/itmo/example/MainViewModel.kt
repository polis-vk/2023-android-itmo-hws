package ru.ok.itmo.example


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import ru.ok.itmo.example.model.MainRepository
import ru.ok.itmo.example.model.Message


class MainViewModel : ViewModel() {

    private val repository = MainRepository()
    val channels = repository.channels
    val channelsLoadingStatus = repository.channelsLoadingStatus

    val signIn = MutableLiveData<Boolean>()
    val openChannel = MutableLiveData<String?>()
    val toastMessage = MutableLiveData<Int?>()

    val DEBUG_LOGIN = "name"
    val DEBUG_PASSWORD = "JOStatiCmAiNqUeUe"


    fun verifyAndLogin(login: String, password: String) {
//        val login = DEBUG_LOGIN
//        val password = DEBUG_PASSWORD
        repository.verifyAndLogin(login, password, signIn) { code, exception ->
            toastMessage.value = when (code) {
                null -> R.string.login_error_cant_connect
                401 -> R.string.login_error_unauthorized
                else -> R.string.login_error
            }
        }
    }

    fun getChannelMessages(
        channel: String,
        callback: (Array<Message>?) -> Unit,
        limit: Int = 20,
        lastKnownId: Int = 0,
        reverse: Boolean = false
    ) {
        repository.getChannelMessages(
            channel,
            callback,
            limit,
            lastKnownId,
            reverse
        )
    }

    fun updateChannels() {
        repository.updateChannels()
    }

    fun openChannel(name: String) {
        openChannel.value = name
    }

    fun logout() {
        repository.logout()
    }
}