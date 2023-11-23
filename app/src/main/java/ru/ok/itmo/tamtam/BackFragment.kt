package ru.ok.itmo.tamtam

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels

class BackFragment : Fragment(R.layout.fragment_back) {

    private val sharedViewModel: SharedViewModel by viewModels(ownerProducer = { requireActivity() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionBarLogic()
    }

    private fun actionBarLogic() {
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar?.title = getString(R.string.action_bar_back_title)
        actionBar?.setBackgroundDrawable(
            ColorDrawable(
                Helper.getColor(
                    requireContext(), R.color.white
                )
            )
        )
        actionBar?.elevation = 0f
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                sharedViewModel.logout()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}