package ru.ok.itmo.tuttut.messenger.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ru.ok.itmo.tuttut.MainActivity
import ru.ok.itmo.tuttut.R

@AndroidEntryPoint
class MessengerFragment : Fragment() {

    companion object {
        fun newInstance() = MessengerFragment()
    }

    private val viewModel: MessengerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_messenger, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = MessengerFragmentArgs.fromBundle(requireArguments()).chat
        viewModel.inbox(name)
        (activity as MainActivity?)?.supportActionBar?.title = name
    }
}