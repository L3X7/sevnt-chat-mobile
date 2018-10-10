package com.sevnt.alex.sevntchat.adapters

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.sevnt.alex.sevntchat.R
import com.sevnt.alex.sevntchat.models.ContactModel
import com.squareup.picasso.Picasso

class ContactAdapter(private val contactModel: List<ContactModel>) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ContactAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.adapter_contact, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return contactModel.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: ContactModel = contactModel[position]
        Picasso.get().load(model.imgUrl)
                .error(R.drawable.ic_error)
                .into(holder.imageUserContact)
        holder.textNameContact.text = model.nameUser

        holder.layoutContact.setOnClickListener {
//            Toast.makeText(holder.layoutContact.context, "Prueba", Toast.LENGTH_LONG).show()
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageUserContact = itemView.findViewById(R.id.imgContactUserImage) as ImageView
        val textNameContact = itemView.findViewById(R.id.txtContactUserName) as TextView
        val layoutContact = itemView.findViewById(R.id.cLayoutContact) as ConstraintLayout
    }

}