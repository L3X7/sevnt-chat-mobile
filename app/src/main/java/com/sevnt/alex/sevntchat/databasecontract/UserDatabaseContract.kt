package com.sevnt.alex.sevntchat.databasecontract

import android.provider.BaseColumns

object UserDatabaseContract {
    class UserEntry : BaseColumns {
        companion object {
            val TABLE_NAME = "users"
            val COLUMN_ID ="_id"
            val ID_USER = "idUser"
            val USER_NAME = "userName"
            val FIRST_NAME = "firstName"
            val SURNAME = "surname"
        }
    }
}