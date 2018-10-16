package com.sevnt.alex.sevntchat.adapters

import android.content.Context
import android.content.Intent
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.sevnt.alex.sevntchat.R
import com.sevnt.alex.sevntchat.activities.ChatActivity
import com.sevnt.alex.sevntchat.activities.UserSearchInformationActivity
import com.sevnt.alex.sevntchat.models.ContactListModel
import com.squareup.picasso.Picasso

class SearchContactListAdapter(private val contactListModel: List<ContactListModel>, private val context: Context) : RecyclerView.Adapter<SearchContactListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): SearchContactListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_contact_list, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return contactListModel.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val listModel: ContactListModel = contactListModel[position]
        Picasso.get().load(listModel.imgUrl)
                .error(R.drawable.ic_error)
                .into(holder.imageUserContact)
        holder.textNameContact.text = listModel.nameUser
        holder.layoutContact.setOnClickListener {
            val intent = Intent(context, UserSearchInformationActivity()::class.java)
            intent.putExtra("username", listModel.userName)
            intent.putExtra("idContact", listModel.idUser)
            intent.putExtra("imgContact", listModel.imgUrl)
            intent.putExtra("nameUserContact", listModel.nameUser)
            context.startActivity(intent)
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageUserContact = itemView.findViewById(R.id.imgContactUserImage) as ImageView
        val textNameContact = itemView.findViewById(R.id.txtContactUserName) as TextView
        val layoutContact = itemView.findViewById(R.id.layoutAdapterContact) as ConstraintLayout

    }

}