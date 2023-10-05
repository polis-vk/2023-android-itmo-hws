package ru.ok.itmo.example.content

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import ru.ok.itmo.example.R
import ru.ok.itmo.example.databinding.FragmentInnerBinding

class InnerFragment : Fragment() {

    companion object {
        fun newInstance(number: Int) = InnerFragment().apply {
            arguments = bundleOf("number" to number.toString())
        }
    }

    private lateinit var bindings: FragmentInnerBinding
    private val viewModel by viewModels<InnerViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_inner, container, false).also { view ->
            bindings = FragmentInnerBinding.bind(view)
            viewModel.color.observe(viewLifecycleOwner) {
                bindings.rootContainer.setBackgroundColor(it)
            }
            viewModel.number.observe(viewLifecycleOwner) {
                bindings.text.text = it.toString()
            }
        }
    }
}