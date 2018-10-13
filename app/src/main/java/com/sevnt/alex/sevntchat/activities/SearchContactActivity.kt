package com.sevnt.alex.sevntchat.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sevnt.alex.sevntchat.R
import com.sevnt.alex.sevntchat.adapters.ContactListAdapter
import com.sevnt.alex.sevntchat.adapters.SearchContactListAdapter
import com.sevnt.alex.sevntchat.helpers.UserDataBaseHelper
import com.sevnt.alex.sevntchat.models.ContactListModel
import com.sevnt.alex.sevntchat.models.UserDBModel
import org.json.JSONObject
import java.lang.Exception

class SearchContactActivity : AppCompatActivity() {
    private lateinit var btnSearchContact: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactListModel: ArrayList<ContactListModel>
    private lateinit var searchContactListAdapter: SearchContactListAdapter
    private var userDBModel: UserDBModel? = null
    private  lateinit var txtSearchContact: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_contact)

        val dbHandler = UserDataBaseHelper(this)
        userDBModel = dbHandler.findFirstUser()
        if (userDBModel == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            this.finish()

        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        txtSearchContact = findViewById(R.id.txtSearchContact)
        contactListModel = ArrayList()
        btnSearchContact = findViewById(R.id.btnSearchContact)
        btnSearchContact.setOnClickListener {
            searchContacts(this)
        }

        recyclerView = findViewById(R.id.rViewSearchContact)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if (id == android.R.id.home) {
            this.finish()
        }
        return super.onOptionsItemSelected(item)

    }


    private fun searchContacts(context: Context) {
        contactListModel = ArrayList()
        val txtSearchValue = txtSearchContact.text
        if(txtSearchValue.toString() == ""){
            Toast.makeText(context, R.string.error_empty_values_search, Toast.LENGTH_SHORT).show()
            return
        }
        val url = resources.getString(R.string.find_users) + txtSearchValue
        val queue = Volley.newRequestQueue(context)
        try {
            val jsonRequest = JsonObjectRequest(Request.Method.GET, url, null,
                    Response.Listener<JSONObject> { response ->
                        if (response != null) {
                            val users = response.getJSONArray("users")
                            if (users.length() > 0) {
                                for (i in 0..(users.length() - 1)) {
                                    val nameContact = users.getJSONObject(i).getString("first_name") + " " + users.getJSONObject(i).getString("surname")
                                    val imageContact = users.getJSONObject(i).getString("user_image")
                                    val idContact = users.getJSONObject(i).getString("_id")
                                    contactListModel.add(ContactListModel(imageContact, nameContact, idContact))
                                }
                            }
                            else{
                                Toast.makeText(context, R.string.register_not_found, Toast.LENGTH_SHORT).show()
                            }
                            searchContactListAdapter = SearchContactListAdapter(contactListModel, context)
                            recyclerView.adapter = searchContactListAdapter
                            recyclerView.adapter?.notifyDataSetChanged()

                        } else {
                            searchContactListAdapter = SearchContactListAdapter(contactListModel, context)
                            recyclerView.adapter = searchContactListAdapter
                            recyclerView.adapter?.notifyDataSetChanged()
                            Toast.makeText(context, R.string.register_not_found, Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener {
                        Toast.makeText(context, R.string.error_request, Toast.LENGTH_SHORT).show()
                    })
            queue.add(jsonRequest)
        } catch (exception: Exception) {
            Toast.makeText(context, R.string.error_request, Toast.LENGTH_SHORT).show()
        }

    }
}
