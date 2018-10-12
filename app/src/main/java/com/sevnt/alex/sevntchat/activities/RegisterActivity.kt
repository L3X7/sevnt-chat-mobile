package com.sevnt.alex.sevntchat.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.sevnt.alex.sevntchat.R

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}
