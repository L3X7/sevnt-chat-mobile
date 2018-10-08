package com.sevnt.alex.sevntchat.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.sevnt.alex.sevntchat.R
import com.sevnt.alex.sevntchat.models.ChatModel
import com.squareup.picasso.Picasso

class ChatAdapter(private val chatDataModel: List<ChatModel>) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_chat, parent, false)
        return ViewHolder(v)
    }


    override fun getItemCount(): Int {
        return chatDataModel.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: ChatModel = chatDataModel[position]
        Picasso.get().load(model.imgUrl)
                .error(R.drawable.ic_error)
                .into(holder.imageChat)
        holder.textTitle.text = model.title
        holder.textDescription.text = model.description
        holder.textHour.text = model.hour


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imageChat = itemView.findViewById(R.id.imgChatUser) as ImageView
        val textTitle = itemView.findViewById(R.id.txtChatTitle) as TextView
        val textDescription = itemView.findViewById(R.id.txtChatDescription) as TextView
        val textHour = itemView.findViewById(R.id.txtChatHour) as TextView

    }
}