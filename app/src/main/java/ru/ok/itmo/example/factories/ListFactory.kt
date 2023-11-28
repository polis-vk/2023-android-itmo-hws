package ru.ok.itmo.example.factories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.ok.itmo.example.list.ListViewModel
import ru.ok.itmo.example.login.LoginViewModel
import ru.ok.itmo.example.repositories.Repository

class ListFactory(
    private val repository: Repository
) :
    ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            return ListViewModel(repository) as T
        }
        throw IllegalArgumentException("Cannot create instance for class ViewModelClass")
    }
}