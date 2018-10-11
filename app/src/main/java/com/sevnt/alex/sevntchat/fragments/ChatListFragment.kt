package com.sevnt.alex.sevntchat.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sevnt.alex.sevntchat.R
import com.sevnt.alex.sevntchat.adapters.ChatListAdapter
import com.sevnt.alex.sevntchat.models.ChatListModel

class ChatListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatListModel: ArrayList<ChatListModel>
    private lateinit var chatListAdapter: ChatListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadInformation()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_chat_list, container, false)
        recyclerView = v.findViewById(R.id.rViewChat)
        chatListAdapter = ChatListAdapter(chatListModel)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = chatListAdapter
        chatListAdapter.notifyDataSetChanged()
        return v
    }

    private fun loadInformation() {
        chatListModel = ArrayList()



    }

}