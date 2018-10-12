package com.sevnt.alex.sevntchat.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.sevnt.alex.sevntchat.R
import com.sevnt.alex.sevntchat.activities.LoginActivity
import com.sevnt.alex.sevntchat.adapters.ContactListAdapter
import com.sevnt.alex.sevntchat.helpers.UserDataBaseHelper
import com.sevnt.alex.sevntchat.models.ContactListModel
import com.sevnt.alex.sevntchat.models.UserDBModel
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class ContactListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactListModel: ArrayList<ContactListModel>
    private lateinit var contactListAdapter: ContactListAdapter
    private var userDBModel: UserDBModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contactListModel = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_contact_list, container, false)

        val dbHandler = UserDataBaseHelper(v.context as Context)
        userDBModel = dbHandler.findFirstUser()
        if(userDBModel == null){
            val intent = Intent(v.context, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()

        }

        recyclerView = v.findViewById(R.id.rViewContact)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        loadContacts(v)
        contactListAdapter = ContactListAdapter(contactListModel, v.context)
        recyclerView.adapter = contactListAdapter
        return v
    }

    private fun loadContacts(itemView: View) {
        val url = resources.getString(R.string.get_contact_by_iuser) + userDBModel?.idUser
        val queue = Volley.newRequestQueue(itemView.context)
        contactListModel = ArrayList()
        try {
            val jsonRequest = JsonArrayRequest(Request.Method.GET, url, null,
                    Response.Listener<JSONArray> { response ->
                        if (response != null) {
                            for (i in 0..(response.length() - 1)) {
                                val jsonObject = response.getJSONObject(i).getJSONObject("contact_user")
                                val nameContact = jsonObject.getString("first_name") + " " + jsonObject.getString("surname")
                                val imageContact = jsonObject.getString("user_image")
                                val idContact = jsonObject.getString("_id")
                                contactListModel.add(ContactListModel(imageContact, nameContact, idContact))
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