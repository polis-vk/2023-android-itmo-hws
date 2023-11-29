package ru.ok.itmo.tamtam.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ok.itmo.tamtam.App
import ru.ok.itmo.tamtam.databinding.FragmentContactsBinding
import ru.ok.itmo.tamtam.presentation.rv.adapter.ContactAdapter
import ru.ok.itmo.tamtam.presentation.stateholder.ContactsState
import ru.ok.itmo.tamtam.presentation.stateholder.ContactsViewModel
import ru.ok.itmo.tamtam.utils.FragmentWithBinding
import ru.ok.itmo.tamtam.utils.getThemeColor
import ru.ok.itmo.tamtam.utils.observeNotifications
import ru.ok.itmo.tamtam.utils.setStatusBarTextDark
import javax.inject.Inject
import ru.ok.itmo.tamtam.Constants.API_AVATAR_URL

class ContactsFragment :
    FragmentWithBinding<FragmentContactsBinding>(FragmentContactsBinding::inflate) {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val contactsViewModel: ContactsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ContactsViewModel::class.java]
    }
    private val contactAdapter: ContactAdapter by lazy { ContactAdapter() }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onViewCreated(view, savedInstanceState)
        setupStatusBar()
        setupRecyclerView()
        observeChatsState()
        this.requireContext().observeNotifications(
            viewLifecycleOwner.lifecycleScope,
            contactsViewModel.notifications
        )
        lifecycleScope.launch {
            delay(100)
            startPostponedEnterTransition()
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun observeChatsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            contactsViewModel.contactsState.collect {
                when (it) {
                    is ContactsState.Idle -> {
                        contactAdapter.submitList(it.contacts)
                        binding.contactsRV.visibility = View.VISIBLE
                        binding.loadingPB.visibility = View.INVISIBLE
                        binding.noContactTV.visibility =
                            if (it.contacts.isEmpty()) View.VISIBLE else View.INVISIBLE
                        binding.errorTV.visibility = View.INVISIBLE
                    }

                    ContactsState.Loading -> {
                        binding.contactsRV.visibility = View.INVISIBLE
                        binding.loadingPB.visibility = View.VISIBLE
                        binding.errorTV.visibility = View.INVISIBLE
                    }

                    ContactsState.Error -> {
                        binding.contactsRV.visibility = View.INVISIBLE
                        binding.loadingPB.visibility = View.INVISIBLE
                        binding.errorTV.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.contactsRV.adapter = contactAdapter
        contactAdapter.onLoadImageByGlide = { imageView, name ->
            Glide.with(this.requireActivity())
                .load(String.format(API_AVATAR_URL, name))
                .placeholder(imageView.drawable)
                .centerCrop()
                .into(imageView)
        }
        contactAdapter.onClick = { chatId ->
            findNavController().navigate(
                ContactsFragmentDirections.actionContactsFragmentToChatFragment(chatId)
            )
        }
    }

    private fun setupStatusBar() {
        requireActivity().window.statusBarColor =
            requireContext().getThemeColor(androidx.appcompat.R.attr.colorPrimary)
        requireActivity().setStatusBarTextDark(true)
    }
}