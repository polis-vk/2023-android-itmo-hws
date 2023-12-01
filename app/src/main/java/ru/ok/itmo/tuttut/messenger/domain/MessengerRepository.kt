package ru.ok.itmo.tuttut.messenger.domain

interface MessengerRepository {

    suspend fun getInbox(): Result<String>
}