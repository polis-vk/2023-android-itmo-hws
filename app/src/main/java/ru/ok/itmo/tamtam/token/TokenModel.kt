package ru.ok.itmo.tamtam.token

import java.util.concurrent.atomic.AtomicReference

open class TokenModel {
    companion object {
        const val DEFAULT_TOKEN = "default_token"
    }

    private val tokenAtomicReference: AtomicReference<String> = AtomicReference(DEFAULT_TOKEN)
    var token: String
        get() = tokenAtomicReference.get()
        protected set(value) = tokenAtomicReference.set(value)
}