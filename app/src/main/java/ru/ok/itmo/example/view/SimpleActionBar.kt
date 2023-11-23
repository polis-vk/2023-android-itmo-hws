package ru.ok.itmo.example.view

import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import ru.ok.itmo.example.MainViewModel
import ru.ok.itmo.example.R

class SimpleActionBar : Fragment(R.layout.action_bar_simple) {

    var title: String = ""
    var buttonImage: Int? = null
    var buttonAction: (() -> Unit)? = null

    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageButton>(R.id.buttonBack)
            .setOnClickListener {
                if (parentFragment is ChatsFragment) {
                    viewModel.logout()
                }
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

        update()
    }

    fun update() {
        view?.run {
            findViewById<TextView>(R.id.title).text = title

            findViewById<ImageButton>(R.id.imageButton).run {
                buttonImage.let {
                    if (it != null) {
                        setImageResource(it)
                    }
                    visibility = if (it == null) View.GONE else View.VISIBLE
                }
                buttonAction.let { if (it != null) setOnClickListener { it() } }
            }
        }
    }
}

/**
 * Grabs SimpleActionBar instance from FragmentContainerView with id = R.id.actionBarContainer.
 * @return SimpleActionBar if it exists, null otherwise
 * */
val View.simpleActionBar: SimpleActionBar?
    get() = this.findViewById<FragmentContainerView?>(R.id.actionBarContainer)
        ?.getFragment()