package com.sevnt.alex.sevntchat.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.sevnt.alex.sevntchat.R
import com.sevnt.alex.sevntchat.adapters.ChatAdapter
import com.sevnt.alex.sevntchat.helpers.UserDataBaseHelper
import com.sevnt.alex.sevntchat.models.ChatModel
import com.sevnt.alex.sevntchat.models.UserDBModel
import org.json.JSONObject
import java.lang.Exception
import java.util.*

class ChatActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatModel: ArrayList<ChatModel>
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var btnSendMessage: Button
    private var userDBModel: UserDBModel? = null
    private lateinit var userTwo: String
    private lateinit var messageRoom: String
    private lateinit var mSocketIO: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        mSocketIO = IO.socket("https://sevnt-chat-api-socket-io.herokuapp.com")


        val dbHandler = UserDataBaseHelper(this)
        userDBModel = dbHandler.findFirstUser()
        if (userDBModel == null) {
            val intentLogin = Intent(this, LoginActivity::class.java)
            startActivity(intentLogin)
            this.finish()
        }


        val intent = intent
        userTwo = intent.getStringExtra("idContact")


//        mSocketIO.on("get-message-personal", newMessage)
        mSocketIO.on("get-message-personal") { args ->
            val dataMessage = args[0] as JSONObject
            try {
                val status = dataMessage.getInt("status")
                when (status) {
                    resources.getInteger(R.integer.http_status_success) -> {
                        val messageCreated = dataMessage.getJSONObject("messagePersonal")
                        val createdBy = messageCreated.getString("created_by")
                        val imageChat = "https://s3.amazonaws.com/uifaces/faces/twitter/VMilescu/128.jpg"
                        val messageChat = messageCreated.getString("message")
                        addMessage(createdBy, imageChat, messageChat)
                    }
                }
            } catch (exception: Exception) {
                Log.e("Error Socket", exception.toString())
            }
        }

        mSocketIO.connect().on(Socket.EVENT_CONNECT) {

        }.on(Socket.EVENT_DISCONNECT) {

        }

        val toolbar = findViewById<Toolbar>(R.id.chatToolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        btnSendMessage = findViewById(R.id.btnChatSendMessage)
        btnSendMessage.setOnClickListener {
            sendMessage()
        }

        recyclerView = findViewById(R.id.rViewChatInteraction)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        getRoom(this)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if (id == android.R.id.home) {
            this.finish()
        }
        return super.onOptionsItemSelected(item)

    }

    private fun getRoom(context: Context) {
        val jsonObject = JSONObject()
        jsonObject.put("id_one", userDBModel?.idUser)
        jsonObject.put("id_two", userTwo)
        val queue = Volley.newRequestQueue(context)
        val url = resources.getString(R.string.get_post_room_personal)
        try {
            val jsonRequest = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    Response.Listener<JSONObject> { response ->
                        if (response != null) {
                            val requestMessage = response.getInt("status")
                            when (requestMessage) {
                                resources.getInteger(R.integer.http_status_success) -> {
                                    messageRoom = response.getString("room")
                                    mSocketIO.emit("subscribe", messageRoom)
                                    loadChats(context)
                                }
                                resources.getInteger(R.integer.http_status_error) -> {
                                    Toast.makeText(this, R.string.error_get_room_personal, Toast.LENGTH_SHORT).show()
                                    this.finish()
                                }
                                resources.getInteger(R.integer.http_status_not_found) -> {
                                    Toast.makeText(this, R.string.error_get_room_personal, Toast.LENGTH_SHORT).show()
                                    this.finish()
                                }
                                else -> {
                                    Toast.makeText(this, R.string.error_get_room_personal, Toast.LENGTH_SHORT).show()
                                    this.finish()
                                }
                            }

                        } else {
                            Toast.makeText(this, R.string.error_get_room_personal, Toast.LENGTH_SHORT).show()
                            this.finish()
                        }
                    },
                    Response.ErrorListener {
                        Toast.makeText(this, R.string.error_get_room_personal, Toast.LENGTH_SHORT).show()
                        this.finish()
                    })
            queue.add(jsonRequest)
        } catch (exception: Exception) {
            Toast.makeText(this, R.string.error_get_room_personal, Toast.LENGTH_SHORT).show()
            this.finish()
        }
    }

    private fun sendMessage() {
        val txtMessage = findViewById<EditText>(R.id.eTxtChatMessage)
        val jsonObject = JSONObject()
        jsonObject.put("user_one", userDBModel?.idUser)
        jsonObject.put("message", txtMessage.text)
        jsonObject.put("message_room", messageRoom)
        try {
            mSocketIO.emit("create-message-personal", jsonObject)
        }
        catch (exception: Exception){
            Toast.makeText(this, R.string.error_get_room_personal, Toast.LENGTH_SHORT).show()
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
                            if (messagesChat.length() > 0) {
                                for (i in 0..(messagesChat.length() - 1)) {
                                    val messageChat = messagesChat.getJSONObject(i).getString("message")
                                    val imageChat = "https://s3.amazonaws.com/uifaces/faces/twitter/VMilescu/128.jpg"
                                    val createdByChat = messagesChat.getJSONObject(i).getString("created_by")
                                    chatModel.add(ChatModel(createdByChat, imageChat, messageChat))
                                }
                                chatAdapter = ChatAdapter(chatModel, userDBModel?.idUser as String)
                                recyclerView.adapter = chatAdapter
                                recyclerView.adapter?.notifyDataSetChanged()
                                scrollToBottom()
                            } else {
                                chatAdapter = ChatAdapter(chatModel, userDBModel?.idUser as String)
                                recyclerView.adapter = chatAdapter
                                recyclerView.adapter?.notifyDataSetChanged()
                            }

                        } else {
                            chatAdapter = ChatAdapter(chatModel, userDBModel?.idUser as String)
                            recyclerView.adapter = chatAdapter
                            recyclerView.adapter?.notifyDataSetChanged()
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

    private fun scrollToBottom(){
        recyclerView.smoothScrollToPosition(chatAdapter.itemCount - 1)
    }

    private fun addMessage(createdBy: String, imgChat: String, message: String) {
        chatModel.add(ChatModel(createdBy, imgChat, message))
        chatAdapter.notifyItemInserted(chatModel.size - 1)

        scrollToBottom()
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocketIO.disconnect()
        mSocketIO.off("get-message-personal")
    }
}
