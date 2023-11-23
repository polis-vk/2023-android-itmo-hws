package ru.ok.itmo.tamtam.presentation.chatsscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bluelinelabs.conductor.Controller
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.launch
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.common.Constants
import ru.ok.itmo.tamtam.common.hide
import ru.ok.itmo.tamtam.common.show
import ru.ok.itmo.tamtam.data.adapters.ChatAdapter
import ru.ok.itmo.tamtam.presentation.viewmodels.ChatsViewModel

class ChatsController : Controller() {

    private var backButton: ImageButton? = null
    private var newChatButton: ImageButton? = null
    private var recycler: RecyclerView? = null
    private var shimmer: ShimmerFrameLayout? = null
    private var title: TextView? = null
    private var error: TextView? = null
    private val viewModel by lazy {
        ChatsViewModel(router)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.chats_screen, container, false)
        val activity = router.activity as ComponentActivity

        initViews(view)

        recycler?.apply {
            adapter = ChatAdapter(activity.baseContext, emptyList(), viewModel)
            layoutManager =
                LinearLayoutManager(activity.baseContext, LinearLayoutManager.VERTICAL, false)
        }

        backButton?.setOnClickListener {
            handleBack()
        }

        newChatButton?.setOnClickListener {
            viewModel.createChat()
        }

        activity.lifecycleScope.launch {
            signOnViewModel(activity)
        }

        return view
    }

    private suspend fun signOnViewModel(activity: ComponentActivity) {
        viewModel.state.collect {
            when (it) {
                is ChatsScreenState.Error -> {
                    shimmer?.hide()
                    shimmer?.stopShimmer()
                    recycler?.hide()
                    if (!it.fromDb) {
                        viewModel.checkDataBase()
                    } else {
                        error?.text = it.message
                        error?.show()
                    }
                }

                ChatsScreenState.Loading -> {
                    shimmer?.show()
                    shimmer?.startShimmer()
                    recycler?.hide()
                    error?.hide()
                }

                is ChatsScreenState.Success -> {
                    shimmer?.hide()
                    shimmer?.stopShimmer()
                    recycler?.show()
                    error?.hide()
                    recycler?.adapter = ChatAdapter(activity.baseContext, it.chats, viewModel)
                }
            }
        }
    }

    override fun handleBack(): Boolean {
        viewModel.logout(
            router.activity?.baseContext?.getSharedPreferences(
                Constants.SHARED,
                ComponentActivity.MODE_PRIVATE
            )?.getString(Constants.TOKEN, "") ?: ""
        )
        return router.popToRoot()
    }

    private fun initViews(view: View) {
        backButton = view.findViewById(R.id.back_button)
        newChatButton = view.findViewById(R.id.new_chat_button)
        recycler = view.findViewById(R.id.recycler_view)
        shimmer = view.findViewById(R.id.shimmer_layout)
        title = view.findViewById(R.id.title)
        error = view.findViewById(R.id.error_field)
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        destroyViews()
    }

    private fun destroyViews() {
        backButton = null
        newChatButton = null
        recycler = null
        shimmer = null
        title = null
        error = null
    }
}