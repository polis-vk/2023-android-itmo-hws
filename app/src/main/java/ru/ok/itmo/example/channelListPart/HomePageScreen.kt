package ru.ok.itmo.example.channelListPart

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.itmo.example.R
import ru.ok.itmo.example.channelListPart.states.GetChannelListState
import ru.ok.itmo.example.databinding.FragmentHomePageScreenBinding
import java.util.Date

class HomePageScreen : Fragment() {

    private lateinit var binding: FragmentHomePageScreenBinding
    private val channelListViewModel: ChannelListViewModel by viewModels {
        CustomViewModelFactory(
            ChannelListModel()
        )
    }
    private lateinit var channelList: RecyclerView
    private var channels: List<ChannelDataClass> = listOf()
    private var bitmaps: HashMap<String, Bitmap> = hashMapOf()

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
        channelList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

//        channelListViewModel.getChannelList()
//        channelListViewModel.getChannelListState.observe(viewLifecycleOwner){ listState ->
//            when(listState){
//                is GetChannelListState.Failed -> Toast.makeText(requireContext(), listState.error_messenge, Toast.LENGTH_SHORT).show()
//                is GetChannelListState.Loading -> {}
//                is GetChannelListState.Success -> channels = listState.channels
//                else -> {}
//            }
//        }
        channels = listOf(
            ChannelDataClass("AM sadsad", "asdas", Date(150), ""),
            ChannelDataClass(
                "bbb",
                "asdas",
                Date(150),
                "https://god-drakona.ru/wp-content/uploads/2023/02/5-2-2048x1280-1.jpg"
            ),
            ChannelDataClass(
                "sadsdsa",
                "asdas",
                Date(150),
                "https://god-drakona.ru/wp-content/uploads/2023/02/5-2-2048x1280-1.jpg"
            )
        )

        lifecycleScope.launch {
            binding.loadingChatsBar.visibility = View.VISIBLE
            for (ch in channels) {
                if (ch.image_link.isNotBlank()) {
                    bitmaps[ch.title] = getBitmap(ch.image_link)
                }
            }
            channelList.adapter = ChannelListAdapter(channels, bitmaps)
            binding.loadingChatsBar.visibility = View.GONE
        }

        binding.toolbar.setNavigationOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homePageScreen_to_authScreen)
            channelListViewModel.logOut()
        }
    }

    private suspend fun getBitmap(url: String): Bitmap {
        return withContext(Dispatchers.IO) {
            val loading = ImageLoader(requireContext())
            val request = ImageRequest.Builder(requireContext())
                .data(url)
                .build()

            val result = try {
                val drawable = loading.execute(request).drawable
                val mutableBitmap = convertToMutable((drawable as BitmapDrawable).bitmap)
                mutableBitmap
            } catch (e: Throwable) {
                throw IllegalStateException("Error loading image", e)
            }

            result
        }
    }

    private fun convertToMutable(bitmap: Bitmap): Bitmap {
        val mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        bitmap.recycle()
        return mutableBitmap
    }

}