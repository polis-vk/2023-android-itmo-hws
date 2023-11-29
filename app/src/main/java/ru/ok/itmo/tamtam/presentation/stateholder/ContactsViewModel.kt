package ru.ok.itmo.tamtam.presentation.stateholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.ok.itmo.tamtam.data.repository.MessageRepository
import ru.ok.itmo.tamtam.domain.model.Contact
import ru.ok.itmo.tamtam.utils.Resource
import javax.inject.Inject

class ContactsViewModel @Inject constructor(
    private val messageRepository: MessageRepository
) : ViewModel() {
    private val _contactsState: MutableStateFlow<ContactsState> =
        MutableStateFlow(ContactsState.Loading)
    val contactsState: Flow<ContactsState> get() = _contactsState
    val notifications get() = messageRepository.notifications

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val contactsResource = messageRepository.getContacts()
            when (contactsResource) {
                is Resource.Failure -> {
                    _contactsState.emit(
                        ContactsState.Error
                    )
                }

                is Resource.Success -> {
                    _contactsState.emit(
                        ContactsState.Idle(
                            contacts = contactsResource.data
                        )
                    )
                }
            }
        }
    }

}

sealed class ContactsState {
    object Loading : ContactsState()
    object Error : ContactsState()
    data class Idle(
        val contacts: List<Contact>
    ) : ContactsState()
}