package ru.ok.itmo.example

import android.os.Bundle
import android.provider.Telephony.Threads
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory

class MainFragment(val backFragment: Fragment) : Fragment(R.layout.fragment) {
    companion object {
        fun testNet(): String {
            val gson = GsonBuilder()
                .setPrettyPrinting()
                .create()
            val gsonConverterFactory = GsonConverterFactory.create(gson)

            val retrofit = Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(gsonConverterFactory)
                .build()

            val api = retrofit.create(Api::class.java)

            val dataChat = DataChat("int");
            //api.postReq(dataChat)

            val ret = api.getReq().blockingGet()
            return ret.name
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retainInstance = true;
        view.findViewById<Button>(R.id.bt_back).setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .remove(this)
                .add(R.id.fragment_container, backFragment)
                .commit()
        }


        /*val countryList = arrayListOf("India", testNet())


        super.onCreate(savedInstanceState)
        val simpleList = requireActivity().findViewById<ListView>(R.id.list_chat)
        val adl = ArrayAdapter(requireActivity(), R.layout.chat_item, R.id.textView, countryList)
        simpleList.setAdapter(adl)*/
    }
}
