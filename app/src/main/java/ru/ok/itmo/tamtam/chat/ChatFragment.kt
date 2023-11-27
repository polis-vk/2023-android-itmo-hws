package ru.ok.itmo.tamtam.chat

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.helper.Helper
import ru.ok.itmo.tamtam.shared_model.SharedViewModel

class ChatFragment : Fragment(R.layout.fragment_chat) {
    private lateinit var name: String

    private val sharedViewModel: SharedViewModel by viewModels(ownerProducer = { requireActivity() })
    private val modelInstance: ChatModel = ChatModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.let { Helper.setLightStatusBar(requireContext(), it) }

        arguments?.let {
            name =
                it.getString(ARG_NAME) ?: throw IllegalArgumentException("I don't know chat name")
        }
        view.findViewById<TextView>(R.id.fragment_chat_navbar_title).text = name

        val textView = view.findViewById<TextView>(R.id.my_chat_text)
        textView.text = "$name get data? wait"

        view.findViewById<ImageView>(R.id.arrow_left_back).setOnClickListener {
            sharedViewModel.closeAll()
        }

        lifecycleScope.launch {
            modelInstance.getMessage(object : OnDataReadyCallbackString {
                override fun onDataReady(jsonString: String) {
                    val gson = Gson()
                    val jsonArray = gson.fromJson(jsonString, Array<JsonObject>::class.java)
                    var result = ""
                    for (jsonObject in jsonArray) {
                        val from = jsonObject.getAsJsonPrimitive("from").asString
                        val data = jsonObject.getAsJsonObject("data")

                        result += "From: $from, Data: ${data.toString()}\n"
                    }

                    textView.text = result
                }
            })
        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                sharedViewModel.closeAll()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    companion object {
        private const val ARG_NAME = "name"

        @JvmStatic
        fun newInstance(name: String) =
            ChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_NAME, name)
                }
            }
    }
}