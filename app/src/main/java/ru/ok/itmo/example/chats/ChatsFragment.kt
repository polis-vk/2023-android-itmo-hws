package ru.ok.itmo.example.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.ok.itmo.example.R
import ru.ok.itmo.example.chats.recycler.ChatsAdapter
import ru.ok.itmo.example.chats.recycler.ChatsDecorator
import ru.ok.itmo.example.databinding.FragmentChatsBinding
import ru.ok.itmo.example.network.result.ErrorType
import ru.ok.itmo.example.utils.ViewUtils

class ChatsFragment : Fragment(R.layout.fragment_chats) {
    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!
    private val args: ChatsFragmentArgs by navArgs()
    private val viewModel by viewModels<ChatsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewUtils.invisible(
            binding.recyclerView,
            binding.errorMessage,
            binding.emptyListMessage,
            binding.progressIndicator
        )
        setupStates()
        initBackPressed()
        if (viewModel.state.value !in listOf(State.CHATS_LOADED, State.CHATS_LOADED_WITH_ERRORS)) {
            viewModel.loadChatItems(args.token)
        }
    }

    private fun setupStates() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state!!) {
                State.LOADING_CHATS -> onLoading()
                State.CHATS_LOADED_WITH_ERRORS -> onLoadedWithErrors()
                State.CHATS_LOADED -> onLoaded()
                State.ERROR -> onError()
                State.INIT -> {}
            }
        }
    }

    private fun onLoading() {
        ViewUtils.visibleFirst(
            binding.progressIndicator,
            binding.errorMessage,
            binding.emptyListMessage,
            binding.recyclerView
        )
    }

    private fun onLoaded() {
        val chatItems = viewModel.getResultNotNull().getListNotNull()
        if (chatItems.isEmpty()) {
            onEmptyList()
            return
        }
        ViewUtils.visibleFirst(
            binding.recyclerView,
            binding.errorMessage,
            binding.emptyListMessage,
            binding.progressIndicator
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        binding.recyclerView.adapter = ChatsAdapter(chatItems)
        binding.recyclerView.addItemDecoration(ChatsDecorator(requireContext()))
    }

    private fun onLoadedWithErrors() {
        onLoaded()
        toastError(viewModel.getResultNotNull().errorType)
    }

    private fun onError() {
        ViewUtils.visibleFirst(
            binding.errorMessage,
            binding.emptyListMessage,
            binding.recyclerView,
            binding.progressIndicator
        )
        binding.errorMessage.text =
            getString(R.string.error, viewModel.getResultNotNull().errorType.name)
    }

    private fun toastError(errorType: ErrorType) {
        Toast.makeText(
            requireContext(),
            getString(R.string.error, errorType.name),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun onEmptyList() {
        ViewUtils.visibleFirst(
            binding.emptyListMessage,
            binding.recyclerView,
            binding.errorMessage,
            binding.progressIndicator
        )
        binding.emptyListMessage.text = getString(R.string.no_messages)
    }

    private fun initBackPressed() {
        binding.toolbar.setupWithNavController(findNavController())
        binding.toolbar.setNavigationOnClickListener {
            viewModel.logout(args.token)
            findNavController().navigateUp()
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.logout(args.token)
                    if (isEnabled) {
                        isEnabled = false
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }
                }
            }
        )
    }

}