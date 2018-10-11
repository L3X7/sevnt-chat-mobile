package com.sevnt.alex.sevntchat.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.sevnt.alex.sevntchat.R
import com.sevnt.alex.sevntchat.models.ChatListModel
import com.squareup.picasso.Picasso

class ChatListAdapter(private val chatListModel: List<ChatListModel>) : RecyclerView.Adapter<ChatListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_chat_list, parent, false)
        return ViewHolder(v)
    }


    override fun getItemCount(): Int {
        return chatListModel.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listModel: ChatListModel = chatListModel[position]
        Picasso.get().load(listModel.imgUrl)
                .error(R.drawable.ic_error)
                .into(holder.imageChat)
        holder.textTitle.text = listModel.title
        holder.textDescription.text = listModel.description
        holder.textHour.text = listModel.hour


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageChat = itemView.findViewById(R.id.imgChatUser) as ImageView
        val textTitle = itemView.findViewById(R.id.txtChatTitle) as TextView
        val textDescription = itemView.findViewById(R.id.txtChatDescription) as TextView
        val textHour = itemView.findViewById(R.id.txtChatHour) as TextView

    }
}