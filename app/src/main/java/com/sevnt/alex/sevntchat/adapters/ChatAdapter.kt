package com.sevnt.alex.sevntchat.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sevnt.alex.sevntchat.R
import com.sevnt.alex.sevntchat.models.ChatModel

class ChatAdapter(private val chatModel: List<ChatModel>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_chat, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return chatModel.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}