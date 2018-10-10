package com.sevnt.alex.sevntchat.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sevnt.alex.sevntchat.R
import com.sevnt.alex.sevntchat.adapters.ContactAdapter
import com.sevnt.alex.sevntchat.models.ContactModel
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class ContactFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactModel: ArrayList<ContactModel>
    private lateinit var contactAdapter: ContactAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactModel = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_contact, container, false)
        recyclerView = v.findViewById(R.id.rViewContact)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        loadContacts(v)
        contactAdapter = ContactAdapter(contactModel)
        recyclerView.adapter = contactAdapter
        return v
    }

    private fun loadContacts(itemView: View) {
        val idUser = "5bbcf21fc2e5dd0015ff7e1d"
        val url = resources.getString(R.string.get_contact_by_iuser) + idUser
        val queue = Volley.newRequestQueue(itemView.context)
        contactModel = ArrayList()
        try {
            val jsonRequest = JsonArrayRequest(Request.Method.GET, url, null,
                    Response.Listener<JSONArray> { response ->
                        if (response != null) {
                            for (i in 0..(response.length() - 1)) {
                                val jsonObject = response.getJSONObject(i).getJSONObject("contact_user")
                                val nameContact = jsonObject.getString("first_name") + " " + jsonObject.getString("surname")
                                val imageContact = jsonObject.getString("user_image")
                                contactModel.add(ContactModel(imageContact, nameContact))
                            }
                            recyclerView.adapter?.notifyDataSetChanged()
                        } else {
                            Toast.makeText(view?.context, R.string.register_not_found, Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener {
                        Toast.makeText(view?.context, R.string.error_request, Toast.LENGTH_SHORT).show()
                    })
            queue.add(jsonRequest)
        } catch (ex: Exception) {
            Toast.makeText(view?.context, R.string.error_request, Toast.LENGTH_SHORT).show()
        }
    }
}