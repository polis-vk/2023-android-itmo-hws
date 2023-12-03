package ru.ok.itmo.example.channelListPart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.example.R
import ru.ok.itmo.example.channelListPart.states.GetChannelListState
import ru.ok.itmo.example.databinding.FragmentHomePageScreenBinding
import java.util.Date

class HomePageScreen : Fragment() {

    private lateinit var binding: FragmentHomePageScreenBinding
    private val channelListViewModel: ChannelListViewModel by viewModels {
        CustomViewModelFactory(
            ChannelListModel()
        )
    }
    private lateinit var channelList: RecyclerView
    private var channels: List<ChannelDataClass> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomePageScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        channelList = binding.channelList
        channelList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        channelListViewModel.getChannelList()
        channelListViewModel.getChannelListState.observe(viewLifecycleOwner){ listState ->
            when(listState){
                is GetChannelListState.Failed -> Toast.makeText(requireContext(), listState.error_messenge, Toast.LENGTH_SHORT).show()
                is GetChannelListState.Loading -> {}
                is GetChannelListState.Success -> channels = listState.channels
                else -> {}
            }
        }

        channelList.adapter = ChannelListAdapter(channels)

        binding.toolbar.setNavigationOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homePageScreen_to_authScreen)
            channelListViewModel.logOut()
        }
    }
}