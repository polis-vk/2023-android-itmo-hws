package ru.ok.itmo.example.channelListPart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.example.R
import ru.ok.itmo.example.databinding.FragmentHomePageScreenBinding
import java.util.Date

class HomePageScreen : Fragment() {

    private lateinit var binding: FragmentHomePageScreenBinding
    private val channelListViewModel by viewModels<ChannelListViewModel>()
    private lateinit var channelList: RecyclerView
    private var channels: List<ChannelDataClass> = listOf(
        ChannelDataClass("VItya","привет, как дела?", Date(12)),
        ChannelDataClass("NIIII","да, да, да", Date(13))

    )

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
        channelList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        channelList.adapter = ChannelListAdapter(channels)

        binding.toolbar.setNavigationOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homePageScreen_to_authScreen)
            channelListViewModel.logOut()
        }
    }
}