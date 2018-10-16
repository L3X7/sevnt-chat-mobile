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
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sevnt.alex.sevntchat.R
import com.sevnt.alex.sevntchat.activities.LoginActivity
import com.sevnt.alex.sevntchat.adapters.ChatListAdapter
import com.sevnt.alex.sevntchat.helpers.UserDataBaseHelper
import com.sevnt.alex.sevntchat.models.ChatListModel
import com.sevnt.alex.sevntchat.models.UserDBModel
import org.json.JSONObject
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ChatListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatListModel: ArrayList<ChatListModel>
    private lateinit var chatListAdapter: ChatListAdapter
    private var userDBModel: UserDBModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatListModel = ArrayList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_chat_list, container, false)

        val dbHandler = UserDataBaseHelper(v.context as Context)
        userDBModel = dbHandler.findFirstUser()
        if (userDBModel == null) {
            val intent = Intent(v.context, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()

        }

        recyclerView = v.findViewById(R.id.rViewChat)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)
        loadInformation(v.context)
        return v
    }

    private fun loadInformation(context: Context) {
        chatListModel = ArrayList()
        val queue = Volley.newRequestQueue(context)
        val url = resources.getString(R.string.get_chats_by_room) + userDBModel?.idUser
        try {
            val jsonRequest = JsonObjectRequest(Request.Method.GET, url, null,
                    Response.Listener<JSONObject> { response ->
                        if (response != null) {
                            val messagesRoom = response.getJSONArray("messagePersonalRoom")
                            val messages = response.getJSONArray("messagePersonal")
                            if (messagesRoom.length() > 0) {
                                for (i in 0..(messagesRoom.length() - 1)) {
                                    var imgChat: String
                                    var userChat: String
                                    var messageChat: String
                                    var hourChat: String
                                    var idContact :String

                                    val objectRoom = messagesRoom.getJSONObject(i)
                                    val userOneRoom = objectRoom.getJSONObject("user_one")
                                    val userTwoRoom = objectRoom.getJSONObject("user_two")
                                    val objectMessage = messages.getJSONObject(i)

                                    if (userDBModel?.idUser != userOneRoom.getString("_id")) {
                                        imgChat = userOneRoom.getString("user_image")
                                        userChat = userOneRoom.getString("first_name") + " " + userOneRoom.getString("surname")
                                        idContact = userOneRoom.getString("_id")

                                    } else {
                                        imgChat = userTwoRoom.getString("user_image")
                                        userChat = userTwoRoom.getString("first_name") + " " + userTwoRoom.getString("surname")
                                        idContact = userTwoRoom.getString("_id")
                                    }

                                    messageChat = objectMessage.getString("message")
                                    hourChat = objectMessage.getString("created_date")

                                    val localePhone = Locale.getDefault()
                                    val inputDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",localePhone)
                                    val outPutDate = SimpleDateFormat("dd/MM/yy hh:mm")
                                    val stringToDate = inputDate.parse(hourChat).toLocaleString()
//                                    val stringOutPut = outPutDate.format(stringToDate)


                                    chatListModel.add(ChatListModel(imgChat, userChat, messageChat, stringToDate, idContact))
                                }
                                chatListAdapter = ChatListAdapter(chatListModel, context)
                                recyclerView.adapter = chatListAdapter
                                chatListAdapter.notifyDataSetChanged()
                            } else {
                                chatListAdapter = ChatListAdapter(chatListModel, context)
                                recyclerView.adapter = chatListAdapter
                                chatListAdapter.notifyDataSetChanged()
                            }

                        } else {
                            chatListAdapter = ChatListAdapter(chatListModel, context)
                            recyclerView.adapter = chatListAdapter
                            chatListAdapter.notifyDataSetChanged()
                            Toast.makeText(view?.context, R.string.register_not_found, Toast.LENGTH_SHORT).show()
                        }
                    },
                    Response.ErrorListener {
                        Toast.makeText(view?.context, R.string.error_request, Toast.LENGTH_SHORT).show()
                    })

            queue.add(jsonRequest)
        } catch (exception: Exception) {
            Toast.makeText(view?.context, R.string.error_request, Toast.LENGTH_SHORT).show()
        }


    }

}