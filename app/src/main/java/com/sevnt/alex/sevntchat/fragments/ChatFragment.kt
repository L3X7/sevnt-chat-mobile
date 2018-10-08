package com.sevnt.alex.sevntchat.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sevnt.alex.sevntchat.R
import com.sevnt.alex.sevntchat.adapters.ChatAdapter
import com.sevnt.alex.sevntchat.models.ChatModel

class ChatFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatModel: ArrayList<ChatModel>
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadInformation()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_chat, container, false)
        recyclerView = v.findViewById(R.id.rViewChat)
        chatAdapter = ChatAdapter(chatModel)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = chatAdapter
        chatAdapter.notifyDataSetChanged()
        return v
    }

    private fun loadInformation() {
        chatModel = ArrayList()
        chatModel.add(
                ChatModel("http://www.menucool.com/slider/prod/image-slider-4.jpg",
                        "Prueba",
                        "Esto es una prueba",
                        "23:02")

        )
        chatModel.add(
                ChatModel("http://www.menucool.com/slider/prod/image-slider-4.jpg",
                        "Prueba",
                        "Esto es una prueba",
                        "23:02"))
        chatModel.add(
                ChatModel("http://www.menucool.com/slider/prod/image-slider-4.jpg",
                        "Prueba",
                        "Esto es una prueba",
                        "23:02"))


    }

}