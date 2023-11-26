package ru.ok.itmo.example.list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import ru.ok.itmo.example.R
import ru.ok.itmo.example.databinding.FragmentListBinding

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var viewModel: ListViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ListViewModel::class.java]

        binding.toolbar.setNavigationOnClickListener {
            val shar = activity?.getSharedPreferences("token_pref", Context.MODE_PRIVATE)!!
            viewModel.logout(shar.getString("token", "").toString())
            shar.edit().remove("token").putString("token", "").apply()
            findNavController().navigate(R.id.action_listFragment_to_startFragment)
        }
        binding.enterButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_chatFragment)
        }
    }

}