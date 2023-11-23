package ru.ok.itmo.tamtam.ui.chats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.databinding.FragmentChatsBinding
import ru.ok.itmo.tamtam.domain.state.ChatsState
import ru.ok.itmo.tamtam.domain.model.ChatsViewModel
import ru.ok.itmo.tamtam.util.ErrorPresenter
import ru.ok.itmo.tamtam.util.TextPresentObjects

class ChatsFragment : Fragment() {
    private val viewModel by viewModels<ChatsViewModel>()
    private lateinit var binding: FragmentChatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllChannels()

        binding.toolbar.setNavigationOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_chatsFragment_to_startFragment)
            viewModel.logout()
        }

        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewModel.chatsState.observe(viewLifecycleOwner) {
            when (val result = it) {
                is ChatsState.Success -> {
                    shorOrHideError(null)
                    if (result.chatInfoList.isEmpty()) {
                        binding.infoText.isVisible = true
                        binding.infoText.text = TextPresentObjects.noneChats
                    } else {
                        binding.infoText.isInvisible = true
                        binding.recyclerView.adapter = ChatsAdapter(result.chatInfoList) { view ->
                            Navigation.findNavController(view)
                                .navigate(R.id.action_chatsFragment_to_communicationFragment)
                        }
                    }
                    binding.loadingPanel.isInvisible = true
                }

                is ChatsState.LoadingChatsInfo -> {
                    shorOrHideError(null)
                    binding.infoText.isInvisible = true
                    binding.loadingPanel.isVisible = true
                }

                is ChatsState.Failure -> {
                    binding.loadingPanel.isInvisible = true
                    binding.infoText.isInvisible = true
                    shorOrHideError(result.throwable)
                }

                else -> {}
            }
        }

    }

    private fun shorOrHideError(error: Throwable?) {
        ErrorPresenter.present(error, binding.errorText)
    }
}
