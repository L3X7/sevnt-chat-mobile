package com.sevnt.alex.sevntchat.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.EditText
import com.sevnt.alex.sevntchat.R

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val toolbar = findViewById<Toolbar>(R.id.chatToolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if(id == android.R.id.home){
            this.finish()
        }
        return super.onOptionsItemSelected(item)

    }

    fun sendMessage(){
        val idUser = "5bbcf21fc2e5dd0015ff7e1d"
        val txtMessage = findViewById<EditText>(R.id.eTxtChatMessage)

    }
}
