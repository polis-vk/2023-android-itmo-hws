package ru.ok.itmo.example.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.ok.itmo.example.DaApplication
import ru.ok.itmo.example.R
import ru.ok.itmo.example.databinding.FragmentListBinding
import ru.ok.itmo.example.factories.ListFactory
import ru.ok.itmo.example.recyclerview.ChannelRecyclerViewAdapter
import ru.ok.itmo.example.repositories.InvalidDataException

class ListFragment : Fragment(){

    private lateinit var binding: FragmentListBinding
    private val viewModel: ListViewModel by viewModels{
        ListFactory((requireActivity().application as DaApplication).repository)
    };



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getInbox()
        viewModel.channels.observe(viewLifecycleOwner){
            try {
                if (it.isEmpty()) {
                    binding.shimmerView.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.noChannelsLayout.visibility = View.VISIBLE
                } else {
                    binding.shimmerView.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.noChannelsLayout.visibility = View.GONE
                    binding.recyclerView.apply {
                        val channelAdapter = ChannelRecyclerViewAdapter(it)
                        layoutManager = LinearLayoutManager(this@ListFragment.context)
                        adapter = channelAdapter
                        channelAdapter.onItemClick = {channel ->
                            val bundle = bundleOf("id" to channel.name!!.removeSuffix("@channel"))
                            findNavController().navigate(R.id.action_listFragment_to_chatFragment, bundle)
                            Log.i("${channel.name}", "Clicked")
                        }
                    }
                }
            } catch (e: InvalidDataException) {
                binding.shimmerView.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
                binding.noChannelsLayout.visibility = View.VISIBLE
                binding.noMessagesText.text = "Ошибка при получении данных"
                binding.noMessagesDescription.text =
                    "Получены неправильные данные, повторите попытку позже"
            }
        }

        binding.createChannel.setOnClickListener {
            val destination = "ULTRAKILL"  // Your channel goes HERE
            viewModel.postTo(destination)
            val bundle = bundleOf("id" to destination)
            findNavController().navigate(R.id.action_listFragment_to_chatFragment, bundle)
        }

        binding.toolbar.setNavigationOnClickListener {
            viewModel.logout()
            findNavController().navigate(R.id.action_listFragment_to_startFragment)
        }
        binding.enterButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_chatFragment)
        }
    }

}