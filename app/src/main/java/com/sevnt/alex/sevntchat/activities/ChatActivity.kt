package com.sevnt.alex.sevntchat.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sevnt.alex.sevntchat.R
import com.sevnt.alex.sevntchat.adapters.ChatAdapter
import com.sevnt.alex.sevntchat.helpers.UserDataBaseHelper
import com.sevnt.alex.sevntchat.models.ChatModel
import com.sevnt.alex.sevntchat.models.UserDBModel
import org.json.JSONObject
import java.lang.Exception

class ChatActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatModel: ArrayList<ChatModel>
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var btnSendMessage: Button
    private var userDBModel: UserDBModel? = null
    private lateinit var userTwo: String
    private lateinit var messageRoom: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val dbHandler = UserDataBaseHelper(this)
        userDBModel = dbHandler.findFirstUser()
        if (userDBModel == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            this.finish()
        }


        val intent = intent
        userTwo = intent.getStringExtra("idContact")
        val toolbar = findViewById<Toolbar>(R.id.chatToolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        btnSendMessage = findViewById(R.id.btnChatSendMessage)
        btnSendMessage.setOnClickListener {
            sendMessage(this)
        }

        recyclerView = findViewById(R.id.rViewChatInteraction)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        loadChats(this)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if (id == android.R.id.home) {
            this.finish()
        }
        return super.onOptionsItemSelected(item)

    }

    private fun sendMessage(context: Context) {
        val txtMessage = findViewById<EditText>(R.id.eTxtChatMessage)
        val jsonObject = JSONObject()
        jsonObject.put("user_one", userDBModel?.idUser)
        jsonObject.put("user_two", userTwo)
        jsonObject.put("message", txtMessage.text)
        jsonObject.put("message_room", messageRoom)
        val queue = Volley.newRequestQueue(context)
        val url = resources.getString(R.string.create_message_personal)
        try {
            val jsonRequest = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    Response.Listener<JSONObject> { response ->
                        if (response != null) {
                            val requestMessage = response.getInt("status")
                            if (requestMessage == 0) {
                                txtMessage.text.clear()
                                loadChats(context)
                                Toast.makeText(context, R.string.message_saved, Toast.LENGTH_SHORT).show()
                                messageRoom = response.getString("messagePersonalRoom")
                            } else {
                                loadChats(context)
                                messageRoom = response.getString("messagePersonalRoom")
                                Toast.makeText(context, R.string.error_request__message_save, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, R.string.error_request__message_save, Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener {
                        Toast.makeText(context, R.string.error_request__message_save, Toast.LENGTH_SHORT).show()
                    })
            queue.add(jsonRequest)

        } catch (exception: Exception) {
            Toast.makeText(context, R.string.error_request__message_save, Toast.LENGTH_SHORT).show()
        }


    }

    private fun loadChats(context: Context) {
        chatModel = ArrayList()
        val queue = Volley.newRequestQueue(context)
        val url = resources.getString(R.string.get_personal_messages_by_id) + "id_one=" + userDBModel?.idUser + "&" + "id_two=" + userTwo

        try {
            val jsonRequest = JsonObjectRequest(Request.Method.GET, url, null,
                    Response.Listener<JSONObject> { response ->
                        if (response != null) {
                            val messagesChat = response.getJSONArray("messagePersonal")
                            if(messagesChat.length() > 0){
                                messageRoom = response.getJSONArray("messagePersonalRoom").getJSONObject(0).getString("_id")
                                for (i in 0..(messagesChat.length() - 1)) {
                                    val messageChat = messagesChat.getJSONObject(i).getString("message")
                                    val imageChat = "https://s3.amazonaws.com/uifaces/faces/twitter/VMilescu/128.jpg"
                                    val createdByChat = messagesChat.getJSONObject(i).getString("created_by")
                                    chatModel.add(ChatModel(createdByChat, imageChat, messageChat))
                                }
                                chatAdapter = ChatAdapter(chatModel, userDBModel?.idUser as String)
                                recyclerView.adapter = chatAdapter
                                recyclerView.adapter?.notifyDataSetChanged()
                            }
                            else{
                                messageRoom = "0"
                                chatAdapter = ChatAdapter(chatModel, userDBModel?.idUser as String)
                                recyclerView.adapter = chatAdapter
                                recyclerView.adapter?.notifyDataSetChanged()
                            }

                        } else {
                            chatAdapter = ChatAdapter(chatModel, userDBModel?.idUser as String)
                            recyclerView.adapter = chatAdapter
                            recyclerView.adapter?.notifyDataSetChanged()
                            messageRoom = "0"
                            Toast.makeText(context, R.string.register_not_found, Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener {
                        Toast.makeText(context, R.string.error_request, Toast.LENGTH_SHORT).show()
                    })
            queue.add((jsonRequest))
        } catch (ex: Exception) {
            Toast.makeText(context, R.string.error_request, Toast.LENGTH_SHORT).show()
        }
    }
}
