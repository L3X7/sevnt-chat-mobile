package com.sevnt.alex.sevntchat.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.sevnt.alex.sevntchat.R
import com.squareup.picasso.Picasso

class UserSearchInformationActivity : AppCompatActivity() {
    private lateinit var imgViewUserSearchInformation: ImageView
    private lateinit var txtViewNameUserSearchInformation: TextView
    private lateinit var txtViewUserNameUserSearchInformation: TextView
    private lateinit var txtViewLocationUserSearchInformation: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_search_information)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        val intent = intent
        imgViewUserSearchInformation = findViewById(R.id.imgViewUserSearchInformation)
        txtViewNameUserSearchInformation = findViewById(R.id.txtViewNameUserSearchInformation)
        txtViewUserNameUserSearchInformation = findViewById(R.id.txtViewUserNameUserSearchInformation)
        txtViewLocationUserSearchInformation = findViewById(R.id.txtViewLocationUserSearchInformation)

        //Populate information
        Picasso.get().load(intent.getStringExtra("imgContact")).into(imgViewUserSearchInformation)
        txtViewNameUserSearchInformation.text = intent.getStringExtra("nameUserContact")
        txtViewUserNameUserSearchInformation.text = intent.getStringExtra("username")

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item?.itemId
        if (id == android.R.id.home) {
            this.finish()
        }
        return super.onOptionsItemSelected(item)

    }
}
