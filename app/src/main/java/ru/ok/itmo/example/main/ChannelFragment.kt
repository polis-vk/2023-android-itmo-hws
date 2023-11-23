package ru.ok.itmo.example.main

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.ok.itmo.example.MainActivity
import ru.ok.itmo.example.R
import ru.ok.itmo.example.config.App
import ru.ok.itmo.example.domain.Message
import ru.ok.itmo.example.domain.MessagePreview
import ru.ok.itmo.example.model.ChannelMessagesViewModel

class ChannelFragment : Fragment(R.layout.channel_fragment) {
    private lateinit var channelHeader: String
    private lateinit var channelName: String

    private lateinit var channelMessages: ChannelMessagesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        channelName = requireArguments().getString("channelName") ?: "UnknownChannel"
        channelHeader = "%s [%s]".format(
            resources.getString(R.string.channel_description),
            channelName
        )

        channelMessages = ViewModelProvider(this)[ChannelMessagesViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as MainActivity).supportActionBar?.title = resources.getString(R.string.channel_header)
        view.findViewById<TextView>(R.id.channelDescription).text = channelName

        view.findViewById<ProgressBar>(R.id.allMessagesLoading).isVisible = true

        val channelRepository = App.instance.channelRepository
        channelRepository.getChannelMessages(channelName).enqueue(object : Callback<List<Message>> {
            override fun onResponse(
                call: Call<List<Message>>,
                response: Response<List<Message>>
            ) {
                response.body()!!.forEach { msg ->
                    fun textMessageArrived() {
                        channelMessages.messageArrived(
                            MessagePreview(
                                msg.id,
                                msg.from,
                                msg.data.Text?.text,
                                null,
                                msg.time
                            )
                        )
                    }

                    msg.data.Image?.link?.let {
                        channelRepository.getPicture(it).enqueue(object : Callback<String> {
                            override fun onResponse(
                                call: Call<String>,
                                response: Response<String>
                            ) {
                                channelMessages.messageArrived(
                                    MessagePreview(
                                        msg.id,
                                        msg.from,
                                        null,
                                        response.body()!!,
                                        msg.time
                                    )
                                )
                            }

                            override fun onFailure(call: Call<String>, t: Throwable) {}
                        })
                    } ?: textMessageArrived()

                }

                val messagesRecycler = view.findViewById<RecyclerView>(R.id.allMessages)

                channelMessages.messages.observe(viewLifecycleOwner) {
                    view.findViewById<ProgressBar>(R.id.allMessagesLoading).isVisible = false

                    if (channelMessages.isEmpty()) {
                        view.findViewById<TextView>(R.id.noMessagesInChannel).isVisible = true
                    } else {
                        messagesRecycler.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = MessageAdapter(it, 6)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<Message>>, t: Throwable) {}
        })
    }
}
