package ru.ok.itmo.tamtam.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.databinding.FragmentCommunicationBinding
import ru.ok.itmo.tamtam.domain.model.CommunicationViewModel

class CommunicationFragment : Fragment() {
    private val viewModel by viewModels<CommunicationViewModel>()
    private lateinit var binding: FragmentCommunicationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentCommunicationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_communicationFragment_to_chatsFragment)
        }
    }
}
