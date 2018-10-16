package com.sevnt.alex.sevntchat.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.sevnt.alex.sevntchat.R
import com.sevnt.alex.sevntchat.models.ChatModel

class ChatAdapter(private val chatModel: List<ChatModel>, private val createdBy: String) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_chat, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return chatModel.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listModel: ChatModel = chatModel[position]
        if (listModel.createdBy == createdBy) {
            holder.layoutUserOne?.visibility = LinearLayout.VISIBLE
//            Picasso.get().load(listModel.imgUser).error(R.drawable.ic_error)
//                    .into(holder.imgUserOne)
            holder.txtUserOne?.text = listModel.messageText
            holder.layoutUserTwo?.visibility = LinearLayout.GONE

        } else {
            holder.layoutUserTwo?.visibility = LinearLayout.VISIBLE
//            Picasso.get().load(listModel.imgUser).error(R.drawable.ic_error)
//                    .into(holder.imgUserTwo)
            holder.txtUserTwo?.text = listModel.messageText
            holder.layoutUserOne?.visibility = LinearLayout.GONE
        }


    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layoutUserTwo = itemView.findViewById<LinearLayout?>(R.id.lLayoutChatLeft)
//        val imgUserTwo = itemView.findViewById<ImageView?>(R.id.imgViewLeftChatUser)
        val txtUserTwo = itemView.findViewById<TextView?>(R.id.txtViewLeftChatUser)

        val layoutUserOne = itemView.findViewById<LinearLayout?>(R.id.lLayoutChatRight)
//        val imgUserOne = itemView.findViewById<ImageView?>(R.id.imgViewRightChatUser)
        val txtUserOne = itemView.findViewById<TextView?>(R.id.txtViewRightChatUser)


    }
}