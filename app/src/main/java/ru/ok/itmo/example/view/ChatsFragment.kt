package ru.ok.itmo.example.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ok.itmo.example.MainViewModel
import ru.ok.itmo.example.R
import ru.ok.itmo.example.chats.Chat
import ru.ok.itmo.example.chats.ChatsListAdapter
import ru.ok.itmo.example.model.MainRepository


class ChatsFragment : Fragment(R.layout.fragment_chats) {

    private val viewModel: MainViewModel by activityViewModels()
    private var progressBarJob: Job? = null

    lateinit var progressBarLoading: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.simpleActionBar!!.run {
                title = getString(R.string.action_bar_chats)
                buttonImage = R.drawable.ic_action_bar_chats
            }

        val adapter = ChatsListAdapter(viewModel)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.adapter = adapter

        val noChannelsPanel = view.findViewById<ConstraintLayout>(R.id.noChannelsPanel)
        progressBarLoading = view.findViewById(R.id.progressBarLoading)


        viewModel.channels.observe(viewLifecycleOwner) {
            adapter.data = it.map { Chat(it) }.toTypedArray()

            noChannelsPanel.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.channelsLoadingStatus.observe(viewLifecycleOwner) {
            when (it) {
                MainRepository.LOADING_STATUS.LOADING -> {
                    noChannelsPanel.visibility = View.GONE
                    progressBarLoading.visibility = View.VISIBLE
                    setProgressBarEnabled(true)
                }
                MainRepository.LOADING_STATUS.LOADED -> {
                    progressBarLoading.visibility = View.GONE
                    setProgressBarEnabled(false)
                }
                MainRepository.LOADING_STATUS.ERROR -> {
                    noChannelsPanel.visibility = View.GONE
                    progressBarLoading.visibility = View.GONE
                    setProgressBarEnabled(false)
                    viewModel.toastMessage.value = R.string.cant_load_channels
                }
            }
        }

        viewModel.updateChannels()
    }

    fun setProgressBarEnabled(enabled: Boolean) {
        progressBarJob?.cancel()
        if (enabled) {
            progressBarJob = CoroutineScope(Dispatchers.Main).launch {
                var i = 1
                while (true) {
                    progressBarLoading.progress = (i++) % 100
                    delay(10)
                }
            }
        }
    }
}