package com.sevnt.alex.sevntchat.activities

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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

class LoginActivity : AppCompatActivity() {
    private lateinit var txtRegister: TextView
    private lateinit var btnLogin: Button
    private lateinit var txtLoginUser: EditText
    private lateinit var txtLoginPassword: EditText
    private var userDBModel: UserDBModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        txtRegister = findViewById(R.id.txtLoginRegister)
        btnLogin = findViewById(R.id.btnLogin)
        txtLoginUser = findViewById(R.id.txtLoginUser)
        txtLoginPassword = findViewById(R.id.txtLoginPassword)


        val dbHandler = UserDataBaseHelper(this)
        userDBModel = dbHandler.findFirstUser()
        if (userDBModel != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }


        btnLogin.setOnClickListener {
            login(this)
        }

        txtRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    private fun login(context: Context) {
        val userNameLogin = txtLoginUser.text.toString()
        val userPasswordLogin = txtLoginPassword.text.toString()
        if (userNameLogin != "" || userPasswordLogin != "") {
            val jsonObject = JSONObject()
            jsonObject.put("username", userNameLogin)
            jsonObject.put("password", userPasswordLogin)
            val url = resources.getString(R.string.login_user)
            var queue = Volley.newRequestQueue(context)
            try {
                val jsonRequest = JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        Response.Listener<JSONObject> { response ->
                            if (response != null) {
                                val responseJson = response.getInt("status")
                                val userObject = response.getJSONArray("user")
                                val statusRequest = resources.getInteger(R.integer.http_status_success)
                                if (responseJson ==  statusRequest) {
                                    if (userObject.length() != 0) {
                                        val idUser = userObject.getJSONObject(0).getString("_id")
                                        val userName = userObject.getJSONObject(0).getString("username")
                                        val firstName = userObject.getJSONObject(0).getString("first_name")
                                        val surName = userObject.getJSONObject(0).getString("surname")

                                        //if exist register get, else create user
                                        val dbHandler = UserDataBaseHelper(context)
                                        val userFindModel = dbHandler.findUserById(idUser)
                                        if(userFindModel == null){
                                            val userDBModel = UserDBModel(idUser,userName,firstName,surName)
                                            dbHandler.addUser(userDBModel)
                                        }

                                        val intent = Intent(context, MainActivity::class.java)
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(context, R.string.error_invalid_credentials_login, Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(context, R.string.error_request_login, Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, R.string.error_request_login, Toast.LENGTH_SHORT).show()
                            }
                        },
                        Response.ErrorListener {
                            Toast.makeText(context, R.string.error_request_login, Toast.LENGTH_SHORT).show()
                        })
                queue.add(jsonRequest)
            } catch (ex: Exception) {
                Toast.makeText(context, R.string.error_request_login, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, R.string.error_empty_values_login, Toast.LENGTH_SHORT).show()
        }
    }
}
