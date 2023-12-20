package ru.ok.itmo.example.chats

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.ColorRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.ok.itmo.example.R
import ru.ok.itmo.example.chats.repository.ImageState
import ru.ok.itmo.example.chats.repository.MessagesState
import ru.ok.itmo.example.chats.retrofit.models.Message
import ru.ok.itmo.example.custom_view.AvatarCustomView

class ChatsFragment : Fragment() {
    companion object {
        const val TAG = "CHATS_FRAGMENT"
    }

    private val chatsViewModel by viewModels<ChatsViewModel>()
    private lateinit var avatarCustomView: AvatarCustomView
    private var firstMessage: Message? = null
    private var imageUrl: String? = null
    private var imageBitmap: Bitmap? = null

    private var currentBackgroundColor = R.color.black
    private var isAvatar = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.chats_fragment, container, false);
        avatarCustomView = view.findViewById(R.id.avatarCustomView)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            chatsViewModel.messages.collect() {
                if (it is MessagesState.Success) {
                    if (it.message.data!!.Image != null) {
                        firstMessage = it.message
                        imageUrl = it.message.data!!.Image!!.link
                        chatsViewModel.getImage(imageUrl!!)
                        setInitials(firstMessage!!.to!!)
                    }
                }
            }
        }
        lifecycleScope.launch {
            chatsViewModel.images.collect() {
                if (it is ImageState.Success) {
                    imageBitmap = it.bitmap
                }
            }
        }
        chatsViewModel.getLastMessage()

        view.findViewById<Button>(R.id.change_view_button).setOnClickListener {
            Log.d(TAG, "Avatar mode $isAvatar")
            if (isAvatar) {
                setInitials(firstMessage?.to ?: "")
            } else {
                if (imageBitmap != null) {
                    setBitmap(imageBitmap!!)
                } else {
                    setBitmapFromRes(R.drawable.kitty)
                }
            }
            isAvatar = !isAvatar
        }

        view.findViewById<Button>(R.id.change_view_background_button).setOnClickListener {
            currentBackgroundColor = if (currentBackgroundColor == R.color.black) {
                R.color.green
            } else {
                R.color.black
            }
            changeBackgroundColor(currentBackgroundColor)
        }
    }

    private fun getInitials(name: String): String {
        if (name.isBlank()) return ""

        val cur = name.split(" ")
        if (cur.size == 1) {
            return if (name[0] != '@') name[0].toString() else ""
        }
        return "${cur[0][0]}${cur[0][1]}"
    }

    private fun setInitials(name: String) {
        avatarCustomView.setText(getInitials(name))
    }

    private fun changeBackgroundColor(@ColorRes colorId: Int) {
        avatarCustomView.setCvBackgroundColor(colorId)
    }

    private fun setBitmap(bitmap: Bitmap) {
        avatarCustomView.setImage(bitmap)
    }

    private fun setBitmapFromRes(resId: Int) {
        avatarCustomView.setImage(BitmapFactory.decodeResource(context?.resources, resId))
    }
}