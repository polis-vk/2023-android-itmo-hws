package ru.ok.itmo.example

object Navigator {
    private var shouldCreateInnerFragmentAction: (String) -> Boolean = { true }
    fun setShouldCreateInnerFragment(shouldCreateInnerFragment: (String) -> Boolean) {
        shouldCreateInnerFragmentAction = shouldCreateInnerFragment
    }

    fun shouldCreateInnerFragment(number: String) = shouldCreateInnerFragmentAction(number)
}