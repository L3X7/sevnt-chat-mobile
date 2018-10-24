package com.sevnt.alex.sevntchat.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.sevnt.alex.sevntchat.MainActivity
import com.sevnt.alex.sevntchat.R
import com.sevnt.alex.sevntchat.helpers.UserDataBaseHelper
import com.sevnt.alex.sevntchat.models.UserDBModel
import org.json.JSONObject
import java.lang.Exception

class RegisterActivity : AppCompatActivity() {
    private lateinit var txtRegisterName: EditText
    private lateinit var txtRegisterSurname: EditText
    private lateinit var txtRegisterUser: EditText
    private lateinit var txtRegisterPassword: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        txtRegisterName = findViewById(R.id.txtRegisterName)
        txtRegisterSurname = findViewById(R.id.txtRegisterSurName)
        txtRegisterUser = findViewById(R.id.txtRegisterUser)
        txtRegisterPassword = findViewById(R.id.txtRegisterPassword)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener {
            register(this)
        }
    }

    private fun register(context: Context) {
        val url = resources.getString(R.string.create_user)
        val queue = Volley.newRequestQueue(context)
        val name = txtRegisterName.text.toString()
        val surName = txtRegisterSurname.text.toString()
        val userName = txtRegisterUser.text.toString()
        val password = txtRegisterPassword.text.toString()
        if (name != "" || surName != "" || userName != "" || password != "") {
            val jsonObject = JSONObject()
            jsonObject.put("username", userName)
            jsonObject.put("password", password)
            jsonObject.put("first_name", name)
            jsonObject.put("surname", surName)
            jsonObject.put("user_image", "https://cdn3.iconfinder.com/data/icons/google-material-design-icons/48/ic_account_circle_48px-512.png")
            try {

                val jsonRequest = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        Response.Listener<JSONObject> { response ->
                            if (response != null) {
                                val responseJson = response.getInt("status")
                                when (responseJson) {
                                    resources.getInteger(R.integer.http_status_success) -> {
                                        val userObject = response.getJSONObject("user")
                                        val idUser = userObject.getString("_id")
                                        val userName = userObject.getString("username")
                                        val firstName = userObject.getString("first_name")
                                        val surName = userObject.getString("surname")
                                        val dbHandler = UserDataBaseHelper(context)
                                        val userDBModel = UserDBModel(idUser, userName, firstName, surName)
                                        dbHandler.addUser(userDBModel)
                                        val intent = Intent(context, MainActivity::class.java)
                                        this.finish()
                                        startActivity(intent)

                                    }
                                    resources.getInteger(R.integer.http_status_bad_request) -> {
                                        Toast.makeText(context, R.string.username_already_exist, Toast.LENGTH_SHORT).show()
                                    }
                                    else -> {
                                        Toast.makeText(context, R.string.error_request_login, Toast.LENGTH_SHORT).show()
                                    }

                                }
                            } else {
                                Toast.makeText(context, R.string.error_request_login, Toast.LENGTH_SHORT).show()
                            }
                        },
                        Response.ErrorListener {
                            Toast.makeText(context, R.string.error_request_login, Toast.LENGTH_SHORT).show()
                        })

                queue.add(jsonRequest)
            } catch (exception: Exception) {
                Toast.makeText(context, R.string.error_request_login, Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(context, R.string.error_empty_values_register, Toast.LENGTH_SHORT).show()

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        this.finish()
    }
}
