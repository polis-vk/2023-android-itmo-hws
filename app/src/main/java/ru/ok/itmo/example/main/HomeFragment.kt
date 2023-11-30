package ru.ok.itmo.example.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.ok.itmo.example.MainActivity
import ru.ok.itmo.example.R
import ru.ok.itmo.example.config.App
import ru.ok.itmo.example.domain.ChannelName
import ru.ok.itmo.example.domain.ChannelPreview
import ru.ok.itmo.example.domain.Message
import ru.ok.itmo.example.domain.MessageData
import ru.ok.itmo.example.domain.MessageDataText
import java.util.Date
import kotlin.concurrent.thread

class HomeFragment : Fragment(R.layout.home_fragment) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).supportActionBar?.title = resources.getString(R.string.home_header)

        val channels = mutableListOf<ChannelPreview>()

        view.findViewById<ProgressBar>(R.id.allChannelsLoading).isVisible = true

        val channelRepository = App.instance.channelRepository
        channelRepository.getAllChannels().enqueue(object : Callback<List<ChannelName>> {
            override fun onResponse(
                call: Call<List<ChannelName>>,
                response: Response<List<ChannelName>>
            ) {
                Log.i("527ea0220035231", "Loaded")

                val channelNames = response.body()!!

                var await = channelNames.size

                channelNames.forEach { channelName ->
                    channelRepository.getChannelMessages(channelName).enqueue(object : Callback<List<Message>> {
                        override fun onResponse(
                            call: Call<List<Message>>,
                            response: Response<List<Message>>
                        ) {
                            val lastMessage = response.body()!!.last()

                            channels += ChannelPreview(channelName,
                                lastMessage.data.Text?.text ?: "<image>",
                                Date(lastMessage.time))

                            await--
                            Log.i("527ea0220035231", "Channel acquired: $channelName")
                        }

                        override fun onFailure(call: Call<List<Message>>, t: Throwable) {}
                    })
                }


                thread {
                    while (await > 0) {
                        Thread.sleep(500)
                    }

                    requireActivity().runOnUiThread {
                        view.findViewById<ProgressBar>(R.id.allChannelsLoading).isVisible = false

                        val channelsRecycler = view.findViewById<RecyclerView>(R.id.allChannels)

                        channelsRecycler.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = ChannelAdapter(channels, 6) {
                                Log.i("527ea0220035231", "$it")
                                parentFragmentManager.beginTransaction()
                                    .add(
                                        R.id.screen_container,
                                        ChannelFragment::class.java,
                                        bundleOf("channelName" to it.name),
                                        "channel"
                                    )
                                    .hide(this@HomeFragment)
                                    .addToBackStack("channel")
                                    .commit()
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<ChannelName>>, t: Throwable) {}
        })

        view.findViewById<Button>(R.id.newChatButton).setOnClickListener {
            val newChatName = "NewChatName"

            channelRepository.createChannel(
                newChatName, Message(
                    123,
                    "Somebody1", "Somebody2",
                    MessageData(MessageDataText("Hello!"), null),
                    System.currentTimeMillis()
                )
            )

            parentFragmentManager.beginTransaction()
                .add(
                    R.id.screen_container,
                    ChannelFragment::class.java,
                    bundleOf("channelName" to newChatName),
                    "channel"
                )
                .hide(this@HomeFragment)
                .addToBackStack("channel")
                .commit()
        }
    }
}