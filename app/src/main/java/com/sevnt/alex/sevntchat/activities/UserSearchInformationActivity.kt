package com.sevnt.alex.sevntchat.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sevnt.alex.sevntchat.R
import com.sevnt.alex.sevntchat.helpers.UserDataBaseHelper
import com.sevnt.alex.sevntchat.models.UserDBModel
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.lang.Exception

class UserSearchInformationActivity : AppCompatActivity() {
    private lateinit var imgViewUserSearchInformation: ImageView
    private lateinit var txtViewNameUserSearchInformation: TextView
    private lateinit var txtViewUserNameUserSearchInformation: TextView
    private lateinit var txtViewLocationUserSearchInformation: TextView
    private lateinit var idContact: String
    private lateinit var btnAddNewContact: Button
    private var userDBModel: UserDBModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_search_information)

        val dbHandler = UserDataBaseHelper(this)
        userDBModel = dbHandler.findFirstUser()
        if (userDBModel == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            this.finish()

        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val intent = intent
        imgViewUserSearchInformation = findViewById(R.id.imgViewUserSearchInformation)
        txtViewNameUserSearchInformation = findViewById(R.id.txtViewNameUserSearchInformation)
        txtViewUserNameUserSearchInformation = findViewById(R.id.txtViewUserNameUserSearchInformation)
        txtViewLocationUserSearchInformation = findViewById(R.id.txtViewLocationUserSearchInformation)
        btnAddNewContact = findViewById(R.id.btnAddNewContact)

        //Populate information
        Picasso.get().load(intent.getStringExtra("imgContact")).into(imgViewUserSearchInformation)
        txtViewNameUserSearchInformation.text = intent.getStringExtra("nameUserContact")
        txtViewUserNameUserSearchInformation.text = intent.getStringExtra("username")
        idContact = intent.getStringExtra("idContact")

        btnAddNewContact.setOnClickListener {
            addContact()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if (id == android.R.id.home) {
            this.finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addContact() {
        if (userDBModel?.idUser == idContact) {
            Toast.makeText(this, R.string.add_contact_not_available, Toast.LENGTH_SHORT).show()
        } else {
            val url = resources.getString(R.string.create_new_contact)
            val jsonObject = JSONObject()
            jsonObject.put("user", userDBModel?.idUser)
            jsonObject.put("contact_user", idContact)
            val queue = Volley.newRequestQueue(this)
            try {
                val jsonRequest = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        Response.Listener<JSONObject> { response ->
                            if (response != null) {
                                val responseJson = response.getInt("status")
                                when (responseJson) {
                                    resources.getInteger(R.integer.http_status_success) -> {
                                        Toast.makeText(this, R.string.contact_createad, Toast.LENGTH_SHORT).show()
                                    }
                                    resources.getInteger(R.integer.http_status_bad_request) -> {
                                        Toast.makeText(this, R.string.contact_already_exist, Toast.LENGTH_SHORT).show()
                                    }
                                    else -> {
                                        Toast.makeText(this, R.string.error_request_login, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(this, R.string.error_request_login, Toast.LENGTH_SHORT).show()
                            }
                        },
                        Response.ErrorListener {
                            Toast.makeText(this, R.string.error_request_login, Toast.LENGTH_SHORT).show()
                        })
                queue.add((jsonRequest))
            } catch (exception: Exception) {
                Toast.makeText(this, R.string.error_request_login, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
