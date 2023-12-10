package ru.ok.itmo.tamTam.chats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.MaterialToolbar
import ru.ok.itmo.tamTam.R


class ChatsFragment : Fragment(R.layout.chats_fragment) {

    private var toolbar: MaterialToolbar? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = view.findViewById(R.id.toolbar)

        toolbar?.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        toolbar = null
    }
}
