package ru.ok.itmo.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import ru.ok.itmo.example.content.InnerFragment
import ru.ok.itmo.example.databinding.FragmentBaseBinding


class BaseFragment : Fragment() {

    private val viewModel by viewModels<BaseViewModel>()
    private lateinit var bindings: FragmentBaseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base, container, false).also {
            bindings = FragmentBaseBinding.bind(it)
            repeat(viewModel.fragmentsCount) { number ->
                bindings.navigationLayout.addView(Button(context).apply {
                    text = number.toString()
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    )
                    val fragment by lazy {
                        InnerFragment.newInstance(number)
                    }
                    setOnClickListener {
                        if (Navigator.shouldCreateInnerFragment(number.toString())) {
                            parentFragmentManager.commit {
                                replace(R.id.inner_fragment, fragment)
                                addToBackStack(number.toString())
                            }
                        }
                    }
                })
            }
        }
    }

    companion object {
        fun newInstance() = BaseFragment()
    }
}