package ru.ok.itmo.tamtam.presentation.chatscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluelinelabs.conductor.Controller
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.launch
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.common.hide
import ru.ok.itmo.tamtam.common.show
import ru.ok.itmo.tamtam.data.adapters.MessageAdapter
import ru.ok.itmo.tamtam.presentation.viewmodels.ChatsScreenViewModel

class ChatListController(bundle: Bundle?) : Controller() {

    private var backButton: ImageButton? = null
    private var recycler: RecyclerView? = null
    private var shimmer: ShimmerFrameLayout? = null
    private var title: TextView? = null
    private var error: TextView? = null
    private val viewModel = ChatsScreenViewModel(bundle?.getString(CHAT_INFO) ?: "1ch")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.chat_list_screen, container, false)
        val activity = router.activity as AppCompatActivity

        initViews(view)

        recycler?.apply {
            adapter = MessageAdapter(activity.baseContext, emptyList())
            layoutManager =
                LinearLayoutManager(activity.baseContext, LinearLayoutManager.VERTICAL, false)
        }

        recycler?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val lastPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                    viewModel.onScrolled(lastPosition, recyclerView.adapter?.itemCount ?: 0) {
                        recyclerView.scrollToPosition(lastPosition)
                    }
                }
            }
        })

        activity.lifecycleScope.launch {
            signOnViewModel(activity)
        }

        backButton?.setOnClickListener {
            handleBack()
        }

        return view
    }

    private fun initViews(view: View) {
        backButton = view.findViewById(R.id.back_button)
        recycler = view.findViewById(R.id.chats_recycler)
        shimmer = view.findViewById(R.id.shimmer_layout)
        title = view.findViewById(R.id.title)
        error = view.findViewById(R.id.error_message)
    }


    private suspend fun signOnViewModel(activity: AppCompatActivity) {
        viewModel.state.collect {
            when (it) {
                is ChatListState.Error -> {
                    shimmer?.stopShimmer()
                    shimmer?.hide()
                    error?.text = it.message
                    error?.show()
                }

                is ChatListState.Success -> {

                    error?.hide()


                    shimmer?.stopShimmer()
                    shimmer?.hide()

                    title?.text = it.chatInfo.title
                    if (it.chatInfo.messages.isEmpty()) {
                        error?.text = activity.getText(R.string.empty_data_received)
                        error?.show()
                    } else {
                        Log.d("Messages_help", it.chatInfo.messages.toString())
                        recycler?.adapter =
                            MessageAdapter(activity.baseContext, it.chatInfo.messages)
                    }

                }

                ChatListState.Loading -> {

                    error?.hide()

                    shimmer?.startShimmer()
                }
            }
        }
    }

    override fun handleBack(): Boolean {
        return router.popCurrentController()
    }

    override fun onDestroyView(view: View) {
        backButton = null
        recycler = null
        shimmer = null
        title = null
        error = null
        super.onDestroyView(view)
    }

    companion object {
        private const val CHAT_INFO = "chat_info"
        fun newInstance(chat: String): ChatListController {
            val bundle = Bundle()
            bundle.putString(CHAT_INFO, chat)
            return ChatListController(bundle)
        }
    }

}