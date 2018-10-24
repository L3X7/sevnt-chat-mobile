package com.sevnt.alex.sevntchat.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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
    private lateinit var txtMessage: EditText
    private lateinit var btnSendMessage: Button
    private var userDBModel: UserDBModel? = null
    private lateinit var userTwo: String
    private lateinit var messageRoom: String
    private lateinit var mSocketIO: Socket
    private lateinit var nameOtherUSer: String
    private var isTyping = false

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

        txtMessage = findViewById(R.id.eTxtChatMessage)

        val intent = intent
        userTwo = intent.getStringExtra("idContact")
        nameOtherUSer = intent.getStringExtra("nameUser")

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
                    resources.getInteger(R.integer.http_status_error) -> {
                        Log.e("Error Socket", "Error controlled")
                    }
                }
            } catch (exception: Exception) {
                Log.e("Error Socket", exception.toString())
            }
        }

        mSocketIO.on("typing") { args ->
            val dataTyping = args[0] as JSONObject
            try {
                val userMessage = dataTyping.getString("message")
                val idUserTyping = dataTyping.getString("id")
                if (idUserTyping != userDBModel?.idUser) {
                    addTypingBar(userMessage)
                } else {
                    removeTypingBar()
                }


            } catch (exception: Exception) {
                removeTypingBar()
            }
        }
        mSocketIO.on("stop_typing") {args ->
            val dataTyping = args[0] as JSONObject
            try {
                val idUserTyping = dataTyping.getString("id")
                if (idUserTyping != userDBModel?.idUser) {
                    removeTypingBar()
                }
            }catch (exception: Exception){
                removeTypingBar()
            }
        }

        mSocketIO.connect().on(Socket.EVENT_CONNECT) {

        }.on(Socket.EVENT_DISCONNECT) {

        }

        val toolbar = findViewById<Toolbar>(R.id.chatToolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = nameOtherUSer


        btnSendMessage = findViewById(R.id.btnChatSendMessage)
        btnSendMessage.setOnClickListener {
            sendMessage()
        }


        txtMessage.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (txtMessage == null) return
                if (!mSocketIO.connected()) {
                    removeTypingBar()
                    isTyping = false
                    return
                }
                val textMessage = txtMessage.text.toString()
                if(textMessage != ""){
                    if(!isTyping){
                        isTyping = true
                        addTyping()
                    }
                }
                else{
                    isTyping = false
                    removeTyping()
                }


            }

        })

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
        supportActionBar?.subtitle = ""
        if (txtMessage.text.toString() == "") {
            Toast.makeText(this, R.string.insert_message, Toast.LENGTH_SHORT).show()
            return
        }
        isTyping = false
        val jsonObject = JSONObject()
        val message = txtMessage.text.toString()
        jsonObject.put("user_one", userDBModel?.idUser)
        jsonObject.put("message", message)
        jsonObject.put("message_room", messageRoom)
        try {
            mSocketIO.emit("create-message-personal", jsonObject)
            txtMessage.text.clear()
        } catch (exception: Exception) {
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

    private fun scrollToBottom() {
        recyclerView.smoothScrollToPosition(chatAdapter.itemCount - 1)
    }

    private fun addMessage(createdBy: String, imgChat: String, message: String) {
        chatModel.add(ChatModel(createdBy, imgChat, message))
        chatAdapter.notifyItemInserted(chatModel.size - 1)
        scrollToBottom()
    }

    private fun removeTypingBar() {
        supportActionBar?.subtitle = ""
    }

    private fun addTypingBar(textUser: String) {
        supportActionBar?.subtitle = textUser
    }

    private fun removeTyping() {
        val jsonObject = JSONObject()
        val idUser = userDBModel?.idUser
        jsonObject.put("message_room", messageRoom)
        jsonObject.put("id", idUser)
        mSocketIO.emit("stop_typing", jsonObject)
    }

    private fun addTyping() {
        val jsonObject = JSONObject()
        val nameUser = userDBModel?.firstName + " " + userDBModel?.surname
        val idUser = userDBModel?.idUser
        jsonObject.put("message_room", messageRoom)
        jsonObject.put("user", nameUser)
        jsonObject.put("id", idUser)
        mSocketIO.emit("typing", jsonObject)
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocketIO.disconnect()
        mSocketIO.off("create-message-personal")
        mSocketIO.off("get-message-personal")
        mSocketIO.off("typing")
    }

}
